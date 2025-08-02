package com.kuzepa.mydates.ui.activities.home.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.DateFormatRule
import com.kuzepa.mydates.domain.usecase.validation.rules.TextFieldRequiredRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val dateFormatProvider: DateFormatProvider,
    private val textFieldRequiredRule: TextFieldRequiredRule,
    private val dateFormatRule: DateFormatRule,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val eventId: Int? = savedStateHandle.get<Int>("id")
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()
    private lateinit var dateMask: String
    private var dateDelimiter by Delegates.notNull<Char>()

    private val savingEventChannel = Channel<SavingEvent>()
    val savingEvents = savingEventChannel.receiveAsFlow()

    init {
        dateDelimiter = dateFormatProvider.getDelimiter()
        loadEventTypes()
        if (eventId == null) {
            dateMask = dateFormatProvider.getShowingMask(_uiState.value.hideYear)
            setDefaultEventType()
        } else {
            fillEventById(eventId)
        }
    }

    private fun loadEventTypes() {
        viewModelScope.launch {
            // TODO fill event type list
        }
    }

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            is EventScreenEvent.ImageChanged -> {
                _uiState.value = _uiState.value.copy(image = event.image)
            }

            is EventScreenEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(name = event.name)
                val validationResult: ValidationResult = textFieldRequiredRule.validate(event.name)
                _uiState.value = _uiState.value.copy(
                    nameValidationError = validationResult.getErrorMessage()
                )
            }

            is EventScreenEvent.DateChanged -> {
                _uiState.value = _uiState.value.copy(date = event.dateInput)
                if (_uiState.value.dateValidationError != null
                    || dateFormatProvider.dateIsFilled(
                        inputDate = event.dateInput,
                        hideYear = _uiState.value.hideYear
                    )
                ) {
                    validateDate(dateInput = event.dateInput)
                }
            }

            is EventScreenEvent.DateFieldHasLostFocus -> {
                validateDate(dateInput = _uiState.value.date)
            }

            is EventScreenEvent.HideYearChanged -> {
                _uiState.value = _uiState.value.copy(hideYear = event.hideYear)
            }

            is EventScreenEvent.EventTypeChanged -> {
                _uiState.value = _uiState.value.copy(eventTypeName = event.eventTypeName)
                val validationResult: ValidationResult =
                    textFieldRequiredRule.validate(event.eventTypeName)
                _uiState.value = _uiState.value.copy(
                    eventTypeValidationError = validationResult.getErrorMessage()
                )
            }

            is EventScreenEvent.LabelsChanged -> {
                _uiState.value = _uiState.value.copy(labels = event.labels)
            }

            is EventScreenEvent.NotesChanged -> {
                _uiState.value = _uiState.value.copy(notes = event.notes)
            }

            is EventScreenEvent.Save -> {
                val hasError = listOf(
                    _uiState.value.nameValidationError,
                    _uiState.value.dateValidationError,
                    _uiState.value.eventTypeValidationError
                ).any { it != null && !it.isEmpty() }
                if (hasError) return
                // TODO save event

                viewModelScope.launch {
                    savingEventChannel.send(SavingEvent.Success)
                }
            }
        }
    }

    private fun validateDate(dateInput: String) {
        val validationResult: ValidationResult = dateFormatRule.validate(
            input = dateInput, hideYear = _uiState.value.hideYear
        )
        _uiState.value = _uiState.value.copy(
            dateValidationError = validationResult.getErrorMessage()
        )
    }

    private fun setDefaultEventType() {
        viewModelScope.launch {
            try {
                val defaultEventType = eventTypeRepository.getDefaultEventType()
                if (defaultEventType != null) {
                    _uiState.value = _uiState.value.copy(eventTypeName = defaultEventType.name)
                }
            } catch (e: Exception) {
                // TODO handle error (just do not fill default event type value)
            }
        }
    }

    private fun fillEventById(id: Int) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getEventById(id = id)
                if (event != null) fillEventFields(event)
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    private fun fillEventFields(event: Event) {
        with(event) {
            val formattedDate = dateFormatProvider.getFormattedDate(
                date,
                DateShowingMode.EDIT_MODE
            )
            _uiState.value = _uiState.value.copy(
                name = name,
                date = formattedDate,
                eventTypeName = type.name,
                notes = notes,
                image = image,
                labels = labels,
                event = event
            )
            dateMask = dateFormatProvider.getShowingMask(_uiState.value.hideYear)
        }
    }

    fun getDateMask(): String = dateMask
    fun getMaskDelimiter(): Char = dateDelimiter

    fun validate() {
        //Validation process

        // If event is valid then cast uiState to event
//        val event = _uiState.value.toEvent()
//        _uiState.value = _uiState.value.copy(event = event)
//        // save event?
//        saveEvent()
    }

    fun saveEvent() {
        viewModelScope.launch {
//            eventRepository.addEvent()
        }
    }

//    private fun EventUiState.toEvent(): Event {
//        val eventDate: EventDate? = date.toEventDate()
//        if (eventDate == null) { /* TODO mark as invalid and return */
//        }
//        return Event(
//            name = name,
//            eventDate = date.toEventDate(),
//            eventType = eventType,
//
//            )
//    }
}

sealed class SavingEvent {
    object Success : SavingEvent()
}