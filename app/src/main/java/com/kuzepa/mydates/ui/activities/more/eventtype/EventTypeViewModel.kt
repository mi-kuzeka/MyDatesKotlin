package com.kuzepa.mydates.ui.activities.more.eventtype

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
import com.kuzepa.mydates.ui.common.baseeditor.BaseEditorViewModel
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

@HiltViewModel
class EventTypeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<EventTypeUiState, EventTypeScreenEvent>() {
    private val eventTypeId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(EventTypeUiState())
    override val uiState: StateFlow<EventTypeUiState> = _uiState.asStateFlow()

    /**
     * Required to check if the name is duplicated
     */
    private var allEventTypes: List<EventType> = emptyList()

    private val savingEventTypeChannel = Channel<ObjectSaving>()
    override val savingFlow = savingEventTypeChannel.receiveAsFlow()

    private val deletingEventTypeChannel = Channel<ObjectDeleting>()
    override val deletingFlow = deletingEventTypeChannel.receiveAsFlow()


    init {
        _uiState.value = _uiState.value.copy(isNewEventType = eventTypeId == null)
        loadEventTypes()
    }

    private fun loadEventTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventTypeRepository.getAllEventTypes().collect { eventTypes ->
                    allEventTypes = eventTypes
                    if (!_uiState.value.isNewEventType && allEventTypes.isNotEmpty()) {
                        allEventTypes.firstOrNull { it.id == eventTypeId }?.populateScreenFields()
                    }
                }
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    private fun EventType.populateScreenFields() {
        _uiState.value.copy(
            eventType = this,
            name = name,
            isDefault = isDefault,
            showZodiac = showZodiac,
            notificationState = notificationState
        )
    }

    override fun onEvent(event: EventTypeScreenEvent) {
        when (event) {
            is EventTypeScreenEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    name = event.name
                )
                validateName()
            }

            is EventTypeScreenEvent.IsDefaultChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    isDefault = event.isDefault
                )
            }

            is EventTypeScreenEvent.ShowZodiacChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    showZodiac = event.showZodiac
                )
            }


            is EventTypeScreenEvent.ShowNotificationsChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    notificationState = if (event.showNotifications) {
                        NotificationFilterState.FILTER_STATE_ON
                    } else {
                        NotificationFilterState.FILTER_STATE_OFF
                    }
                )
            }

            EventTypeScreenEvent.Save -> {
                if (isValid()) save()
            }

            EventTypeScreenEvent.Delete -> {
                delete()
            }
        }
    }

    private fun validateName() {
        val validationResult: ValidationResult =
            validateNameNotEmptyAndDistinct(
                input = _uiState.value.name,
                nameList = allEventTypes.map { it.name }
            )
        _uiState.value = _uiState.value.copy(
            nameValidationError = validationResult.getErrorMessage()
        )
    }

    override fun isValid(): Boolean {
        validateName()
        return _uiState.value.nameValidationError == null
    }

    override fun save() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    with(_uiState.value) {
                        eventTypeRepository.upsertEventType(
                            EventType(
                                id = eventType?.id ?: UUID.randomUUID().toString(),
                                name = name,
                                isDefault = isDefault,
                                notificationState = notificationState,
                                showZodiac = showZodiac
                            )
                        )
                    }
                }
                savingEventTypeChannel.send(ObjectSaving.Success)
            } catch (e: Exception) {
                // TODO handle error
                savingEventTypeChannel.send(ObjectSaving.Error(e.message.toString()))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    eventTypeRepository.deleteEventTypeById(_uiState.value.eventType?.id ?: "")
                }
                deletingEventTypeChannel.send(ObjectDeleting.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingEventTypeChannel.send(ObjectDeleting.Error(e.message.toString()))
            }
        }
    }
}