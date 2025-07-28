package com.kuzepa.mydates.ui.activities.home.event

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val dateFormatProvider: DateFormatProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val eventId: Int? = savedStateHandle.get<Int>("id")
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        loadEventTypes()
        if (eventId == null) {
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

    private fun setDefaultEventType() {
        viewModelScope.launch {
            try {
                val defaultEventType = eventTypeRepository.getDefaultEventType()
                if (defaultEventType != null) {
                    _uiState.value = _uiState.value.copy(eventType = defaultEventType.name)
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
                eventType = type.name,
                notes = notes,
                image = image,
                labels = labels,
                event = event
            )
        }
    }

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

data class EventUiState(
    val event: Event? = null,
    val name: String = "",
    val nameValidationError: String? = null,
    val date: String = "",
    val dateValidationError: String? = null,
    val eventType: String = "",
    val eventTypeValidationError: String? = null,
    val notes: String = "",
    val image: Bitmap? = null,
    val labels: List<Label> = emptyList(),
    val eventTypes: List<EventType> = emptyList()
)