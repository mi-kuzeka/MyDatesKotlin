package com.kuzepa.mydates.feature.home.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.common.util.image.getRotatedImage
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.hasYear
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.EditorResultEvent
import com.kuzepa.mydates.domain.usecase.image.DeleteCachedImageUseCase
import com.kuzepa.mydates.domain.usecase.image.GetImageFromCacheUseCase
import com.kuzepa.mydates.domain.usecase.label.LabelsFetching
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val labelRepository: LabelRepository,
    private val dateFormatProvider: DateFormatProvider,
    private val validateTextNotEmpty: ValidateTextNotEmptyUseCase,
    private val validateDate: ValidateDateUseCase,
    private val getImageFromCache: GetImageFromCacheUseCase,
    private val deleteCachedImage: DeleteCachedImageUseCase,
    private val navigationDialogResult: NavigationDialogResult,
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

    init {
        loadEventTypes()

        _uiState.update { it.copy(isNewEvent = eventId == null) }
        eventId?.let { fillEventById(it) } ?: setDefaultEventType()
        observeDialogResults()
    }

    fun loadEventTypes() {
        viewModelScope.launch {
            val eventTypes = eventTypeRepository.getAllEventTypes().firstOrNull() ?: emptyList()
            _uiState.update { it.copy(availableEventTypes = eventTypes) }
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
            try {
                val allLabels = labelRepository.getAllLabels().firstOrNull() ?: emptyList()
                val eventLabelIds = _uiState.value.labels.mapTo(HashSet()) { it.id }
                _uiState.update { state ->
                    state.copy(
                        dropdownLabels = allLabels.filter { label -> label.id !in eventLabelIds }
                            .toList()
                    )
                }

                fetchingLabelsSharedFlow.emit(LabelsFetching.Success)
            } catch (e: Exception) {
                // TODO handle error
                fetchingLabelsSharedFlow.emit(LabelsFetching.Error(e.message.toString()))
            }
        }
    }

    private fun handleEventTypeResult(eventTypeId: String?) {
        eventTypeId?.let { id ->
            viewModelScope.launch {
                try {
                    val newEventType = eventTypeRepository.getEventTypeById(id)
                    newEventType?.let {
                        addEventTypeToListAndSelect(newEventType)
                    }
                } catch (e: Exception) {
                    // TODO handle error
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
                try {
                    val newLabel = labelRepository.getLabelById(id)
                    newLabel?.let {
                        if (_uiState.value.labels.firstOrNull { it.id == newLabel.id } == null) {
                            addLabelToList(newLabel)
                        } else {
                            updateLabel(newLabel)
                        }
                    }
                } catch (e: Exception) {
                    // TODO handle error
                }
            }
        }
    }

    fun handleImageCropperResult(imagePath: String?) {
        imagePath?.let { imagePath ->
            viewModelScope.launch {
                val image = getImageFromCache(imagePath)
                if (image.isSuccess) {
                    _uiState.update {
                        it.copy(
                            image = image.getOrNull(),
                            hasChanges = true
                        )
                    }
                    deleteCachedImage(imagePath)
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
                    it.copy(
                        hasChanges = true,
                        hideYear = event.hideYear
                    )
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
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        labels = updatedLabels
                    )
                }
            }

            is EventScreenEvent.RotateImageLeft -> {
                _uiState.value.image?.let { image ->
                    try {
                        val rotatedBitmap = getRotatedImage(image, -90f)
                        _uiState.update {
                            it.copy(
                                image = rotatedBitmap,
                                hasChanges = true
                            )
                        }
                    } catch (_: Exception) {
                    }
                }
            }

            is EventScreenEvent.RotateImageRight -> {
                _uiState.value.image?.let { image ->
                    try {
                        val rotatedBitmap = getRotatedImage(image, 90f)
                        _uiState.update {
                            it.copy(
                                image = rotatedBitmap,
                                hasChanges = true
                            )
                        }
                    } catch (_: Exception) {
                    }
                }
            }

            is EventScreenEvent.DeleteImage -> {
                _uiState.update {
                    it.copy(
                        image = null,
                        hasChanges = true
                    )
                }
            }

            is EventScreenEvent.NewLabelClicked -> {
                loadDropDownLabels()
            }

            is EventScreenEvent.NotesChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        notes = event.notes
                    )
                }
            }

            is EventScreenEvent.Save -> {
                if (isValid()) save()
            }

            is EventScreenEvent.Delete -> {
                delete()
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
        val validationResult: ValidationResult =
            validateTextNotEmpty(_uiState.value.name)
        _uiState.update {
            it.copy(
                nameValidationError = validationResult.getErrorMessage()
            )
        }
    }

    private fun validateDate() {
        val validationResult: ValidationResult = validateDate(
            input = _uiState.value.date, hideYear = _uiState.value.hideYear
        )
        _uiState.update {
            it.copy(
                dateValidationError = validationResult.getErrorMessage()
            )
        }
    }

    private fun validateEventType() {
        val validationResult: ValidationResult =
            validateTextNotEmpty(_uiState.value.eventTypeName)
        _uiState.update {
            it.copy(
                eventTypeValidationError = validationResult.getErrorMessage()
            )
        }
    }

    private fun setDefaultEventType() {
        viewModelScope.launch {
            try {
                val defaultEventType = eventTypeRepository.getDefaultEventType()
                defaultEventType?.let {
                    _uiState.update { it.copy(eventTypeName = defaultEventType.name) }
                }
            } catch (e: Exception) {
                // TODO handle error (just do not fill default event type value)
            }
        }
    }

    private fun fillEventById(id: Long) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getEventById(id = id)
                event?.let {
                    event.populateScreenFields()
                }
            } catch (e: Exception) {
                // TODO handle error
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
        val eventDate = dateFormatProvider.getEditedEventDate(
            formattedDate = _uiState.value.date,
            hideYear = _uiState.value.hideYear
        ) ?: run {
            // TODO handle error
            return
        }

        viewModelScope.launch {
            try {
                val eventType = _uiState.value.availableEventTypes
                    .firstOrNull { it.name == _uiState.value.eventTypeName }
                    ?: createAndGetNewEventType(_uiState.value.eventTypeName)

                eventRepository.upsertEvent(
                    with(_uiState.value) {
                        Event(
                            id = event?.id,
                            name = name,
                            date = eventDate,
                            type = eventType,
                            notes = notes,
                            image = image,
                            labels = labels
                        )
                    }
                )
                eventMonth = eventDate.month
                _editorResultEventFlow.emit(EditorResultEvent.SaveSuccess())
            } catch (e: Exception) {
                // TODO handle error
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(e.message.toString()))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch {
            try {
                eventRepository.deleteEventById(_uiState.value.event?.id ?: 0)
                _editorResultEventFlow.emit(EditorResultEvent.DeleteSuccess)
            } catch (e: Exception) {
                // TODO handle error
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(e.message.toString()))
            }
        }
    }
}