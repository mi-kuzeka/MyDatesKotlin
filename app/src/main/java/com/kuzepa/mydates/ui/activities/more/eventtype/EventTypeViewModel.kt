package com.kuzepa.mydates.ui.activities.more.eventtype

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventTypeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val validateTextNotEmpty: ValidateTextNotEmptyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val eventTypeId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(EventTypeUiState())
    val uiState: StateFlow<EventTypeUiState> = _uiState.asStateFlow()

    private val savingEventTypeChannel = Channel<SavingEventType>()
    val savingEvents = savingEventTypeChannel.receiveAsFlow()

    init {
        eventTypeId?.let {
            fillEventTypeById(it)
        }
    }

    private fun fillEventTypeById(id: String) {
        viewModelScope.launch {
            try {
                val eventType = eventTypeRepository.getEventTypeById(id = id)
                if (eventType != null) fillEventTypeFields(eventType)
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    private fun fillEventTypeFields(eventType: EventType) {
        _uiState.value.copy(
            eventType = eventType,
            name = eventType.name,
            isDefault = eventType.isDefault,
            showZodiac = eventType.showZodiac,
            notificationState = eventType.notificationState
        )
    }
}

sealed class SavingEventType {
    object Success : SavingEventType()
}