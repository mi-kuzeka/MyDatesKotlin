package com.kuzepa.mydates.ui.activities.home.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.hasYear
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
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
) : ViewModel() {
    private val eventId: Int? = savedStateHandle.get<Int>("id")
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()
    private var fullDateMask: String
    private var shortDateMask: String
    private lateinit var dateMask: String
    private var dateDelimiter by Delegates.notNull<Char>()

    /**
     *Required to populate the drop-down list
     */
    private var allEventTypes: List<EventType> = emptyList()


    private val savingEventChannel = Channel<SavingEvent>()
    val savingEvent = savingEventChannel.receiveAsFlow()

    private val deletingEventChannel = Channel<DeletingEvent>()
    val deletingEvent = deletingEventChannel.receiveAsFlow()

    init {
        dateDelimiter = dateFormatProvider.getDelimiter()
        fullDateMask = dateFormatProvider.getFullMask()
        shortDateMask = dateFormatProvider.getShortMask()
        setDateMask()
        loadEventTypes()
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

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            is EventScreenEvent.ImageChanged -> {
                _uiState.value = _uiState.value.copy(image = event.image)
            }

            is EventScreenEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(name = event.name)
                validateName()
            }

            is EventScreenEvent.DateChanged -> {
                _uiState.value = _uiState.value.copy(date = event.dateInput)
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
                _uiState.value = _uiState.value.copy(hideYear = event.hideYear)
                if (event.hideYear) {
                    dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                    _uiState.value = _uiState.value.copy(
                        date = dateFormatProvider.getDateWithoutYear(_uiState.value.date)
                    )
                }
                if (_uiState.value.dateValidationError != null
                    || dateFormatProvider.dateIsFilled(
                        inputDate = _uiState.value.date,
                        hideYear = _uiState.value.hideYear
                    )
                ) {
                    validateDate()
                }
                setDateMask()
            }

            is EventScreenEvent.EventTypeChanged -> {
                _uiState.value = _uiState.value.copy(eventTypeName = event.eventTypeName)
                validateEventType()
            }

            is EventScreenEvent.LabelsChanged -> {
                _uiState.value = _uiState.value.copy(labels = event.labels)
            }

            is EventScreenEvent.RemoveLabel -> {
                val updatedLabels = _uiState.value.labels.filter { it.id != event.labelId }
                _uiState.value = _uiState.value.copy(labels = updatedLabels)
            }

            is EventScreenEvent.AddLabel -> {
                val updatedLabels = mutableListOf<Label>()
                updatedLabels.addAll(_uiState.value.labels)
                updatedLabels.add(event.label)
                _uiState.value = _uiState.value.copy(labels = updatedLabels)
            }

            is EventScreenEvent.NotesChanged -> {
                _uiState.value = _uiState.value.copy(notes = event.notes)
            }

            is EventScreenEvent.Save -> {
                validateEvent()
                val hasError = listOf(
                    _uiState.value.nameValidationError,
                    _uiState.value.dateValidationError,
                    _uiState.value.eventTypeValidationError
                ).any { it != null && !it.isEmpty() }
                if (hasError) return

                saveEvent()
            }

            is EventScreenEvent.Delete -> {
                deleteEvent()
            }
        }
    }

    private fun validateEvent() {
        validateName()
        validateDate()
        validateEventType()
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

    private fun saveEvent() {
        val eventDate = dateFormatProvider.getEditedEventDate(
            formattedDate = _uiState.value.date,
            hideYear = _uiState.value.hideYear
        ) ?: run {
            // TODO handle error
            return
        }

        viewModelScope.launch() {
            try {
                val eventType = allEventTypes
                    .firstOrNull { it.name == _uiState.value.eventTypeName }
                    ?: createAndGetNewEventType(_uiState.value.eventTypeName)

                withContext(Dispatchers.IO) {
                    eventRepository.upsertEvent(
                        Event(
                            id = _uiState.value.event?.id ?: 0,
                            name = _uiState.value.name,
                            date = eventDate,
                            type = eventType,
                            notes = _uiState.value.notes,
                            image = _uiState.value.image,
                            labels = _uiState.value.labels
                        )
                    )
                }
                savingEventChannel.send(SavingEvent.Success)
            } catch (e: Exception) {
                // TODO handle error
                savingEventChannel.send(SavingEvent.Error(e.message.toString()))
            }
        }
    }

    private fun deleteEvent() {
        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    eventRepository.deleteEventById(_uiState.value.event?.id ?: 0)
                }
                deletingEventChannel.send(DeletingEvent.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingEventChannel.send(DeletingEvent.Error(e.message.toString()))
            }
        }
    }

    fun isNewEvent() = _uiState.value.event == null

    private suspend fun createAndGetNewEventType(name: String): EventType {
        val eventType = EventType(
            id = UUID.randomUUID().toString(),
            name = name,
            isDefault = false,
            showZodiac = false,
            notificationState = NotificationFilterState.FILTER_STATE_ON
        )
        eventTypeRepository.addEventType(eventType)
        return eventType
    }
}

sealed class SavingEvent {
    object Success : SavingEvent()
    data class Error(val message: String) : SavingEvent()
}

sealed class DeletingEvent {
    object Success : DeletingEvent()
    data class Error(val message: String) : DeletingEvent()
}