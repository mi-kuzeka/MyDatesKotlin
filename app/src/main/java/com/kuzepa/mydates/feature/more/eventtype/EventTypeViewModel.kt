package com.kuzepa.mydates.feature.more.eventtype

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventTypeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    private val navigationDialogResult: NavigationDialogResult,
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
        _uiState.update { it.copy(isNewEventType = eventTypeId == null) }
        loadEventTypes()
    }

    private fun loadEventTypes() {
        viewModelScope.launch {
            try {
                allEventTypes = eventTypeRepository.getAllEventTypes().firstOrNull() ?: emptyList()
                if (!_uiState.value.isNewEventType && allEventTypes.isNotEmpty()) {
                    allEventTypes.firstOrNull { it.id == eventTypeId }?.populateScreenFields()
                }
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    private fun EventType.populateScreenFields() {
        _uiState.update {
            it.copy(
                eventType = this,
                name = name,
                isDefault = isDefault,
                showZodiac = showZodiac,
                notificationState = notificationState
            )
        }
    }

    override fun onEvent(event: EventTypeScreenEvent) {
        when (event) {
            is EventTypeScreenEvent.NameChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        name = event.name
                    )
                }
                validateName()
            }

            is EventTypeScreenEvent.IsDefaultChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        isDefault = event.isDefault
                    )
                }
            }

            is EventTypeScreenEvent.ShowZodiacChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        showZodiac = event.showZodiac
                    )
                }
            }


            is EventTypeScreenEvent.ShowNotificationsChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        notificationState = if (event.showNotifications) {
                            NotificationFilterState.FILTER_STATE_ON
                        } else {
                            NotificationFilterState.FILTER_STATE_OFF
                        }
                    )
                }
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
                nameList = allEventTypes.filter { it.id != eventTypeId }.map { it.name }
            )
        _uiState.update {
            it.copy(
                nameValidationError = validationResult.getErrorMessage()
            )
        }
    }

    override fun isValid(): Boolean {
        validateName()
        return _uiState.value.nameValidationError == null
    }

    override fun save() {
        viewModelScope.launch {
            try {
                val id = _uiState.value.eventType?.id ?: UUID.randomUUID().toString()
                with(_uiState.value) {
                    val eventType = EventType(
                        id = id,
                        name = name,
                        isDefault = isDefault,
                        notificationState = notificationState,
                        showZodiac = showZodiac
                    )
                    if (isDefault) {
                        eventTypeRepository.clearDefaultAndUpsertEventType(eventType)
                    } else {
                        eventTypeRepository.upsertEventType(eventType)
                    }
                }
                navigationDialogResult.setDialogResultData(DialogResultData.EventTypeResult(id))
                savingEventTypeChannel.send(ObjectSaving.Success(id = id))
            } catch (e: Exception) {
                // TODO handle error
                savingEventTypeChannel.send(ObjectSaving.Error(e.message.toString()))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch() {
            try {
                eventTypeRepository.deleteEventTypeById(_uiState.value.eventType?.id ?: "")
                deletingEventTypeChannel.send(ObjectDeleting.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingEventTypeChannel.send(ObjectDeleting.Error(e.message.toString()))
            }
        }
    }
}