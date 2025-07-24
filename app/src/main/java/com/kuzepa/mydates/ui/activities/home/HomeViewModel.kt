package com.kuzepa.mydates.ui.activities.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository
) : ViewModel() {
    private var currentPage = 0 // TODO get current month - 1
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                eventRepository.getEventsByMonth(currentPage + 1)
                    .catch { e ->
                        _uiState.value = HomeUiState.Error
                        Log.e("MainViewModel", "Error loading events", e)
                    }
                    .collect { events ->
                        _uiState.value = HomeUiState.Success(events)
                    }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error
                Log.e("MainViewModel", "Error loading events", e)
            }
        }
    }

    //TODO move this to event view model
    fun createEvent() {
        val testEventType = EventType(
            id = UUID.randomUUID().toString(),
            name = "Birthday",
            isDefault = true,
            notificationState = NotificationFilterState.FILTER_STATE_ON,
            showZodiac = true
        )
        val testEvent = Event(
            name = "Test",
            eventDate = EventDate(1, 1, 2001),
            eventType = testEventType,
            notes = "",
            notificationDate = 101
        )
        viewModelScope.launch(Dispatchers.IO) {
            eventTypeRepository.addEventType(testEventType)
            eventRepository.addEvent(testEvent)
        }
    }

    fun setCurrentPage(page: Int) {
        currentPage = page
        loadEvents()
    }
}