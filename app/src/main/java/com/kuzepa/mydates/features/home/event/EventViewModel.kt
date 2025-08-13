package com.kuzepa.mydates.features.home.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.hasYear
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val dateFormatProvider: DateFormatProvider,
    private val validateTextNotEmpty: ValidateTextNotEmptyUseCase,
    private val validateDate: ValidateDateUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<EventUiState, EventScreenEvent>() {

    private val eventId: Int? = savedStateHandle.get<Int>("id")
    private val _uiState = MutableStateFlow(EventUiState())
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private var fullDateMask: String
    private var shortDateMask: String
    private lateinit var dateMask: String
    private var dateDelimiter by Delegates.notNull<Char>()

    /**
     *Required to populate the drop-down list
     */
    private var allEventTypes: List<EventType> = emptyList()

    private val savingEventChannel = Channel<ObjectSaving>()
    override val savingFlow = savingEventChannel.receiveAsFlow()

    private val deletingEventChannel = Channel<ObjectDeleting>()
    override val deletingFlow = deletingEventChannel.receiveAsFlow()

    init {
        dateDelimiter = dateFormatProvider.getDelimiter()
        fullDateMask = dateFormatProvider.getFullMask()
        shortDateMask = dateFormatProvider.getShortMask()
        setDateMask()
        loadEventTypes()

        _uiState.value = _uiState.value.copy(isNewEvent = eventId == null)
        if (eventId == null) {
            setDefaultEventType()
        } else {
            fillEventById(eventId)
        }
    }

    private fun loadEventTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            eventTypeRepository.getAllEventTypes().collect { eventTypes ->
                allEventTypes = eventTypes
            }
        }
    }

    override fun onEvent(event: EventScreenEvent) {
        when (event) {
            is EventScreenEvent.ImageChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    image = event.image
                )
            }

            is EventScreenEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    name = event.name
                )
                validateName()
            }

            is EventScreenEvent.DateChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    date = event.dateInput
                )
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
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    hideYear = event.hideYear
                )
                if (event.hideYear) {
                    dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                    _uiState.value = _uiState.value.copy(
                        date = dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                    )
                }

                with(_uiState.value) {
                    if (dateValidationError != null
                        || dateFormatProvider.dateIsFilled(inputDate = date, hideYear = hideYear)
                    ) {
                        validateDate()
                    }
                }
                setDateMask()
            }

            is EventScreenEvent.EventTypeChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    eventTypeName = event.eventTypeName
                )
                validateEventType()
            }

            is EventScreenEvent.LabelsChanged -> {
                _uiState.value = _uiState.value.copy(labels = event.labels)
            }

            is EventScreenEvent.RemoveLabel -> {
                val updatedLabels = _uiState.value.labels.filter { it.id != event.labelId }
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    labels = updatedLabels
                )
            }

            is EventScreenEvent.AddLabel -> {
                val updatedLabels = mutableListOf<Label>()
                updatedLabels.addAll(_uiState.value.labels)
                updatedLabels.add(event.label)
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    labels = updatedLabels
                )
            }

            is EventScreenEvent.NotesChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    notes = event.notes
                )
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
        _uiState.value = _uiState.value.copy(
            nameValidationError = validationResult.getErrorMessage()
        )
    }

    private fun validateDate() {
        val validationResult: ValidationResult = validateDate(
            input = _uiState.value.date, hideYear = _uiState.value.hideYear
        )
        _uiState.value = _uiState.value.copy(
            dateValidationError = validationResult.getErrorMessage()
        )
    }

    private fun validateEventType() {
        val validationResult: ValidationResult =
            validateTextNotEmpty(_uiState.value.eventTypeName)
        _uiState.value = _uiState.value.copy(
            eventTypeValidationError = validationResult.getErrorMessage()
        )
    }

    private fun setDefaultEventType() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val defaultEventType = eventTypeRepository.getDefaultEventType()
                defaultEventType?.let {
                    _uiState.value = _uiState.value.copy(eventTypeName = defaultEventType.name)
                }
            } catch (e: Exception) {
                // TODO handle error (just do not fill default event type value)
            }
        }
    }

    private fun fillEventById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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
        _uiState.value = _uiState.value.copy(
            name = name,
            date = formattedDate,
            eventTypeName = type.name,
            hideYear = !date.hasYear(),
            notes = notes,
            image = image,
            labels = labels,
            event = this
        )
        dateMask = dateFormatProvider.getShowingMask(_uiState.value.hideYear)
    }

    fun getAllEventTypes(): List<EventType> = allEventTypes

    fun setDateMask() {
        dateMask = dateFormatProvider.getShowingMask(_uiState.value.hideYear)
    }

    fun getDateMask(): String = dateMask

    fun getMaskDelimiter(): Char = dateDelimiter

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
                val eventType = allEventTypes
                    .firstOrNull { it.name == _uiState.value.eventTypeName }
                    ?: createAndGetNewEventType(_uiState.value.eventTypeName)

                withContext(Dispatchers.IO) {
                    eventRepository.upsertEvent(
                        with(_uiState.value) {
                            Event(
                                id = event?.id ?: 0,
                                name = name,
                                date = eventDate,
                                type = eventType,
                                notes = notes,
                                image = image,
                                labels = labels
                            )
                        }
                    )
                }
                savingEventChannel.send(ObjectSaving.Success)
            } catch (e: Exception) {
                // TODO handle error
                savingEventChannel.send(ObjectSaving.Error(e.message.toString()))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    eventRepository.deleteEventById(_uiState.value.event?.id ?: 0)
                }
                deletingEventChannel.send(ObjectDeleting.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingEventChannel.send(ObjectDeleting.Error(e.message.toString()))
            }
        }
    }
}