package com.kuzepa.mydates.feature.event

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.dateformat.DateFormatterResult
import com.kuzepa.mydates.common.util.image.getRotatedImage
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.common.util.onFailureIfNotCancelled
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.hasYear
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.EditorResultEvent
import com.kuzepa.mydates.domain.usecase.image.DeleteCachedImageUseCase
import com.kuzepa.mydates.domain.usecase.image.GetImageFromCacheUseCase
import com.kuzepa.mydates.domain.usecase.label.LabelsFetching
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.getLogMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val labelRepository: LabelRepository,
    private val dateFormatProvider: DateFormatProvider,
    private val validateTextNotEmpty: ValidateTextNotEmptyUseCase,
    private val validateDate: ValidateDateUseCase,
    private val getImageFromCache: GetImageFromCacheUseCase,
    private val deleteCachedImage: DeleteCachedImageUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<EventUiState, EventScreenEvent>() {

    private val eventId: Long? by lazy { savedStateHandle.get<Long>("id")?.takeIf { it > 0 } }

    private val _uiState = MutableStateFlow(EventUiState())
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()
    private val dateDelimiter: Char by lazy { dateFormatProvider.getDelimiter() }

    private val _editorResultEventFlow = MutableSharedFlow<EditorResultEvent>(replay = 0)
    override val editorResultEventFlow = _editorResultEventFlow.asSharedFlow()

    private val fetchingLabelsSharedFlow = MutableSharedFlow<LabelsFetching>()
    val fetchingLabelsFlow = fetchingLabelsSharedFlow.asSharedFlow()
    var eventMonth: Int? = null

    private val logTag = "EventViewModel"

    init {
        loadEventTypes()

        _uiState.update { it.copy(isNewEvent = eventId == null) }
        eventId?.let { fillEventById(it) } ?: setDefaultEventType()
        observeDialogResults()
    }

    fun loadEventTypes() {
        viewModelScope.launch {
            runCatching {
                eventTypeRepository.getAllEventTypes().firstOrNull() ?: emptyList()
            }.onSuccess { eventTypes ->
                _uiState.update { it.copy(availableEventTypes = eventTypes) }
            }.onFailureIfNotCancelled { e ->
                onError(
                    logMessage = getLogMessage(logTag, "Error getting event types", e),
                    showingMessage = context.resources.getString(R.string.error_getting_event_types)
                )
            }
        }
    }

    private fun observeDialogResults() {
        viewModelScope.launch {
            navigationDialogResult.dialogResultData.collect { result ->
                when (result) {
                    is DialogResultData.ImageCropperResult -> {
                        handleImageCropperResult(result.imagePath)
                        navigationDialogResult.clearDialogResultData()
                    }

                    is DialogResultData.EventLabelResult -> {
                        handleLabelResult(result.id)
                        navigationDialogResult.clearDialogResultData()
                    }

                    is DialogResultData.EventTypeResult -> {
                        handleEventTypeResult(result.id)
                        navigationDialogResult.clearDialogResultData()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun loadDropDownLabels() {
        viewModelScope.launch {
            runCatching {
                labelRepository.getAllLabels().firstOrNull() ?: emptyList()
            }.onSuccess { allLabels ->
                val eventLabelIds = _uiState.value.labels.mapTo(HashSet()) { it.id }
                _uiState.update { state ->
                    state.copy(
                        dropdownLabels = allLabels.filter { label -> label.id !in eventLabelIds }
                            .toList()
                    )
                }
                fetchingLabelsSharedFlow.emit(LabelsFetching.Success)
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error getting tags", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(
                        R.string.error_getting_labels
                    )
                )
                fetchingLabelsSharedFlow.emit(LabelsFetching.Error(errorMessage))
            }
        }
    }

    private fun handleEventTypeResult(eventTypeId: String?) {
        eventTypeId?.let { id ->
            viewModelScope.launch {
                runCatching {
                    eventTypeRepository.getEventTypeById(id)
                }.onSuccess { newEventType ->
                    newEventType?.let {
                        addEventTypeToListAndSelect(newEventType)
                    }
                }.onFailureIfNotCancelled { e ->
                    onError(
                        logMessage = getLogMessage(
                            logTag, "Error getting new event type", e
                        ),
                        showingMessage = context.resources.getString(
                            R.string.error_getting_new_event_type
                        )
                    )
                }
            }
        }
    }

    private fun setEventType(name: String) {
        _uiState.update { it.copy(hasChanges = true, eventTypeName = name) }
    }

    private fun handleLabelResult(labelId: String?) {
        labelId?.let { id ->
            viewModelScope.launch {
                runCatching {
                    labelRepository.getLabelById(id)
                }.onSuccess { newLabel ->
                    newLabel?.let {
                        if (_uiState.value.labels.firstOrNull { it.id == newLabel.id } == null) {
                            addLabelToList(newLabel)
                        } else {
                            updateLabel(newLabel)
                        }
                    }
                }.onFailureIfNotCancelled { e ->
                    onError(
                        logMessage = getLogMessage(
                            logTag, "Error getting new or updated tag", e
                        ),
                        showingMessage = context.resources.getString(
                            R.string.error_getting_new_label
                        )
                    )
                }
            }
        }
    }

    fun handleImageCropperResult(imagePath: String?) {
        imagePath?.let { imagePath ->
            viewModelScope.launch {
                getImageFromCache(imagePath)
                    .onSuccess { image ->
                        _uiState.update { it.copy(image = image, hasChanges = true) }

                        deleteCachedImage(imagePath)
                            .onFailureIfNotCancelled { e ->
                                onError(
                                    logMessage = getLogMessage(
                                        tag = logTag,
                                        title = "Error getting new or updated tag",
                                        throwable = e
                                    )
                                )
                            }
                    }.onFailureIfNotCancelled { e ->
                        onError(
                            logMessage = getLogMessage(
                                logTag, "Image cropping error", e
                            ),
                            showingMessage = context.resources.getString(
                                R.string.error_cropping_image
                            )
                        )
                    }
            }
        }
    }

    private fun addEventTypeToListAndSelect(eventType: EventType) {
        _uiState.update {
            it.copy(
                hasChanges = true,
                availableEventTypes = it.availableEventTypes + eventType,
                eventTypeName = eventType.name
            )
        }
    }

    private fun addLabelToList(label: Label) {
        _uiState.update {
            it.copy(
                hasChanges = true,
                labels = it.labels + label
            )
        }
    }

    private fun updateLabel(newLabel: Label) {
        _uiState.update {
            it.copy(
                labels = it.labels.map { oldLabel ->
                    if (oldLabel.id == newLabel.id) newLabel else oldLabel
                }
            )
        }
    }

    override fun onEvent(event: EventScreenEvent) {
        when (event) {
            is EventScreenEvent.ImageChanged -> {
                _uiState.update { it.copy(hasChanges = true, image = event.image) }
            }

            is EventScreenEvent.NameChanged -> {
                _uiState.update { it.copy(hasChanges = true, name = event.name) }
                validateName()
            }

            is EventScreenEvent.DateChanged -> {
                _uiState.update { it.copy(hasChanges = true, date = event.dateInput) }
                if (_uiState.value.dateValidationError != null
                    || dateFormatProvider.dateIsFilled(
                        inputDate = event.dateInput,
                        hideYear = _uiState.value.hideYear
                    )
                ) {
                    validateDate()
                }
            }

            is EventScreenEvent.DateFieldHasLostFocus -> {
                validateDate()
            }

            is EventScreenEvent.HideYearChanged -> {
                _uiState.update {
                    it.copy(hasChanges = true, hideYear = event.hideYear)
                }
                if (event.hideYear) {
                    dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                    _uiState.update {
                        it.copy(
                            date = dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                        )
                    }
                }

                with(_uiState.value) {
                    if (dateValidationError != null
                        || dateFormatProvider.dateIsFilled(inputDate = date, hideYear = hideYear)
                    ) {
                        validateDate()
                    }
                }
            }

            is EventScreenEvent.EventTypeChanged -> {
                setEventType(event.eventTypeName)
                validateEventType()
            }

            is EventScreenEvent.LabelsChanged -> {
                _uiState.update { it.copy(labels = event.labels) }
            }

            is EventScreenEvent.RemoveLabelFromEvent -> {
                val updatedLabels = _uiState.value.labels.filter { it.id != event.labelId }
                _uiState.update { it.copy(hasChanges = true, labels = updatedLabels) }
            }

            is EventScreenEvent.RotateImageLeft -> {
                _uiState.value.image?.let { image ->
                    runCatching {
                        getRotatedImage(image, -90f)
                    }.onSuccess { rotatedBitmap ->
                        _uiState.update { it.copy(image = rotatedBitmap, hasChanges = true) }
                    }.onFailure { e ->
                        viewModelScope.launch {
                            onError(
                                logMessage = getLogMessage(logTag, "Error rotating image left", e),
                                showingMessage = context.resources.getString(R.string.error_rotating_image)
                            )
                        }
                    }
                }
            }

            is EventScreenEvent.RotateImageRight -> {
                _uiState.value.image?.let { image ->
                    runCatching {
                        getRotatedImage(image, 90f)
                    }.onSuccess { rotatedBitmap ->
                        _uiState.update { it.copy(image = rotatedBitmap, hasChanges = true) }
                    }.onFailure { e ->
                        viewModelScope.launch {
                            onError(
                                logMessage = getLogMessage(logTag, "Error rotating image right", e),
                                showingMessage = context.resources.getString(R.string.error_rotating_image)
                            )
                        }
                    }
                }
            }

            is EventScreenEvent.DeleteImage -> {
                _uiState.update { it.copy(image = null, hasChanges = true) }
            }

            is EventScreenEvent.NewLabelClicked -> {
                loadDropDownLabels()
            }

            is EventScreenEvent.NotesChanged -> {
                _uiState.update { it.copy(hasChanges = true, notes = event.notes) }
            }

            is EventScreenEvent.Save -> {
                if (isValid()) save()
            }

            is EventScreenEvent.Delete -> {
                delete()
            }

            is EventScreenEvent.ClearError -> {
                clearError()
            }
        }
    }

    override fun isValid(): Boolean {
        validateName()
        validateDate()
        validateEventType()

        return listOf(
            _uiState.value.nameValidationError,
            _uiState.value.dateValidationError,
            _uiState.value.eventTypeValidationError
        ).all { it == null }
    }

    private fun validateName() {
        val validationResult: ValidationResult = validateTextNotEmpty(_uiState.value.name)
        _uiState.update {
            it.copy(nameValidationError = validationResult.getErrorMessage())
        }
    }

    private fun validateDate() {
        val validationResult: ValidationResult = validateDate(
            input = _uiState.value.date, hideYear = _uiState.value.hideYear
        )
        validationResult.getLogMessage()?.let { logMessage ->
            viewModelScope.launch {
                onError(logMessage = logMessage)
            }
        }
        _uiState.update {
            it.copy(dateValidationError = validationResult.getErrorMessage())
        }
    }

    private fun validateEventType() {
        val validationResult: ValidationResult =
            validateTextNotEmpty(_uiState.value.eventTypeName)
        _uiState.update {
            it.copy(eventTypeValidationError = validationResult.getErrorMessage())
        }
    }

    private fun setDefaultEventType() {
        viewModelScope.launch {
            runCatching {
                eventTypeRepository.getDefaultEventType()
            }.onSuccess { defaultEventType ->
                defaultEventType?.let {
                    _uiState.update { it.copy(eventTypeName = defaultEventType.name) }
                }
            }.onFailureIfNotCancelled { e ->
                onError(
                    logMessage = getLogMessage(logTag, "Error setting default event type", e),
                    showingMessage = context.resources.getString(R.string.error_setting_default_event_type)
                )
            }
        }
    }

    private fun fillEventById(id: Long) {
        viewModelScope.launch {
            runCatching {
                eventRepository.getEventById(id = id)
            }.onSuccess { event ->
                event?.let { event.populateScreenFields() }
            }.onFailureIfNotCancelled { e ->
                onError(
                    logMessage = getLogMessage(logTag, "Error filling event fields", e),
                    showingMessage = context.resources.getString(R.string.error_filling_event_fields)
                )
            }
        }
    }

    private fun Event.populateScreenFields() {
        val formattedDate = dateFormatProvider.getEditedDateString(date)
        _uiState.update {
            it.copy(
                name = name,
                date = formattedDate,
                eventTypeName = type.name,
                hideYear = !date.hasYear(),
                notes = notes,
                image = image,
                labels = labels.toList(),
                event = this
            )
        }
    }

    fun getDateMask(): String = dateFormatProvider.getShowingMask(_uiState.value.hideYear)

    fun getMaskDelimiter(): Char = dateDelimiter

    fun getLabelIdListAsJson(): String =
        Json.encodeToString(_uiState.value.labels.map { it.id }.toList())

    private suspend fun createAndGetNewEventType(name: String): EventType {
        val eventType = EventType(
            id = UUID.randomUUID().toString(),
            name = name,
            isDefault = false,
            showZodiac = false,
            notificationState = NotificationFilterState.FILTER_STATE_ON
        )
        eventTypeRepository.upsertEventType(eventType)
        return eventType
    }

    override fun save() {
        val eventDateResult = dateFormatProvider.getEditedEventDate(
            formattedDate = _uiState.value.date,
            hideYear = _uiState.value.hideYear
        )
        var eventDate: EventDate
        when (eventDateResult) {
            is DateFormatterResult.ShortLength -> {
                viewModelScope.launch {
                    onError(
                        logMessage = getLogMessage(
                            logTag,
                            "Error getting EventDate from string",
                            "String length is less than ${eventDateResult.limit}"
                        ),
                        showingMessage = context.resources.getString(
                            R.string.error_getting_event_date_from_string
                        )
                    )
                }
                return
            }

            is DateFormatterResult.Error -> {
                viewModelScope.launch {
                    onError(
                        logMessage = getLogMessage(
                            logTag,
                            "Error getting EventDate from string",
                            eventDateResult.e
                        ),
                        showingMessage = context.resources.getString(
                            R.string.error_getting_event_date_from_string
                        )
                    )
                }
                return
            }

            is DateFormatterResult.OK -> {
                eventDate = eventDateResult.eventDate
            }
        }

        viewModelScope.launch {
            runCatching {
                _uiState.value.availableEventTypes
                    .firstOrNull { it.name == _uiState.value.eventTypeName }
                    ?: createAndGetNewEventType(_uiState.value.eventTypeName)
            }.mapCatching { eventType ->
                eventRepository.upsertEvent(event = createEventFromUiState(eventDate, eventType))
            }.onSuccess {
                eventMonth = eventDate.month
                _editorResultEventFlow.emit(EditorResultEvent.SaveSuccess())
            }.onFailureIfNotCancelled { e ->
                val errorMessage =
                    getLogMessage(logTag, "Error saving event", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_saving_event)
                )
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(errorMessage))
            }
        }
    }

    private fun createEventFromUiState(
        eventDate: EventDate,
        eventType: EventType
    ): Event {
        return with(_uiState.value) {
            Event(
                id = event?.id,
                name = name.trim(),
                date = eventDate,
                type = eventType,
                notes = notes,
                image = image,
                labels = labels
            )
        }
    }

    override fun delete() {
        viewModelScope.launch {
            runCatching {
                eventRepository.deleteEventById(_uiState.value.event?.id ?: 0)
            }.onSuccess {
                _editorResultEventFlow.emit(EditorResultEvent.DeleteSuccess)
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error deleting event", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_deleting_event)
                )
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(errorMessage))
            }
        }
    }

    override suspend fun onError(logMessage: String, showingMessage: String?) {
        withContext((Dispatchers.IO)) {
            errorLoggerRepository.logError(logMessage)
        }
        showingMessage?.let { message ->
            withContext(Dispatchers.Main) {
                setError(message)
            }
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}