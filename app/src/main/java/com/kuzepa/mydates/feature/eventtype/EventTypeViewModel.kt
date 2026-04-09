package com.kuzepa.mydates.feature.eventtype

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.common.util.onFailureIfNotCancelled
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.EditorResultEvent
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventTypeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<EventTypeUiState, EventTypeScreenEvent>() {
    private val eventTypeId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(EventTypeUiState())
    override val uiState: StateFlow<EventTypeUiState> = _uiState.asStateFlow()

    /**
     * Required to check if the name is duplicated
     */
    private var allEventTypes: List<EventType> = emptyList()

    private val _editorResultEventFlow = MutableSharedFlow<EditorResultEvent>(replay = 0)
    override val editorResultEventFlow = _editorResultEventFlow.asSharedFlow()
    private val logTag = "EventTypeViewModel"


    init {
        _uiState.update { it.copy(isNewEventType = eventTypeId == null) }
        loadEventTypes()
    }

    private fun loadEventTypes() {
        viewModelScope.launch {
            runCatching {
                eventTypeRepository.getAllEventTypes().firstOrNull() ?: emptyList()
            }.onSuccess { eventTypes ->
                allEventTypes = eventTypes
                if (!_uiState.value.isNewEventType && allEventTypes.isNotEmpty()) {
                    allEventTypes.firstOrNull { it.id == eventTypeId }?.populateScreenFields()
                }
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error getting event types", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_getting_event_types)
                )
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

            is EventTypeScreenEvent.Save -> {
                if (isValid()) save()
            }

            is EventTypeScreenEvent.Delete -> {
                delete()
            }

            is EventTypeScreenEvent.ClearError -> {
                clearError()
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
            val id = _uiState.value.eventType?.id ?: UUID.randomUUID().toString()
            runCatching {
                with(_uiState.value) {
                    val eventType = EventType(
                        id = id,
                        name = name.trim(),
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
            }.onSuccess {
                navigationDialogResult.setDialogResultData(DialogResultData.EventTypeResult(id))
                _editorResultEventFlow.emit(EditorResultEvent.SaveSuccess(id = id))
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error saving event type", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_saving_event_type)
                )
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(errorMessage))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch {
            runCatching {
                eventTypeRepository.deleteEventTypeById(_uiState.value.eventType?.id ?: "")
            }.onSuccess {
                _editorResultEventFlow.emit(EditorResultEvent.DeleteSuccess)
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error deleting event type", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_deleting_event_type)
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