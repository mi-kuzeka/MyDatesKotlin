package com.kuzepa.mydates.feature.label

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.common.util.onFailureIfNotCancelled
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
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
class LabelViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val labelRepository: LabelRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<LabelUiState, LabelScreenEvent>() {

    private val labelId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(LabelUiState())
    override val uiState: StateFlow<LabelUiState> = _uiState.asStateFlow()

    /**
     * Required to check if the name is duplicated
     */
    private var allLabels: List<Label> = emptyList()

    private val _editorResultEventFlow = MutableSharedFlow<EditorResultEvent>(replay = 0)
    override val editorResultEventFlow = _editorResultEventFlow.asSharedFlow()
    val logTag = "LabelViewModel"

    init {
        _uiState.update { it.copy(isNewLabel = labelId == null) }
        loadLabels()
        observeDialogResults()
    }

    private fun loadLabels() {
        viewModelScope.launch {
            runCatching {
                labelRepository.getAllLabels().firstOrNull() ?: emptyList()
            }.onSuccess { labels ->
                allLabels = labels
                if (!_uiState.value.isNewLabel && allLabels.isNotEmpty()) {
                    allLabels.firstOrNull { it.id == labelId }?.populateScreenFields()
                }
            }.onFailureIfNotCancelled { e ->
                onError(
                    logMessage = getLogMessage(logTag, "Error getting tags", e),
                    showingMessage = context.resources.getString(R.string.error_getting_labels)
                )
            }
        }
    }

    private fun observeDialogResults() {
        viewModelScope.launch {
            navigationDialogResult.dialogResultData.collect { result ->
                when (result) {
                    is DialogResultData.ColorPickerResult -> {
                        onEvent(LabelScreenEvent.ColorChanged(result.color))
                        navigationDialogResult.clearDialogResultData()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun Label.populateScreenFields() {
        _uiState.update {
            it.copy(
                label = this,
                name = name,
                nameFirstLetter = name.take(1),
                colorId = color,
                icon = LabelIcon.fromId(iconId) ?: LabelIcon.FIRST_LETTER,
                emoji = emoji,
                notificationState = notificationState
            )
        }
    }

    override fun onEvent(event: LabelScreenEvent) {
        when (event) {
            is LabelScreenEvent.NameChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        name = event.name,
                        nameFirstLetter = event.name.take(1)
                    )
                }
                validateName()
            }

            is LabelScreenEvent.ColorChanged -> {
                event.color?.let {
                    _uiState.update {
                        it.copy(
                            hasChanges = true,
                            colorId = event.color
                        )
                    }
                }
            }

            is LabelScreenEvent.IconChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        icon = event.icon
                    )
                }
            }

            is LabelScreenEvent.EmojiPicked -> {
                if (event.emoji == null) {
                    _uiState.update {
                        it.copy(
                            hasChanges = true,
                            icon = LabelIcon.EMOJI
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            hasChanges = true,
                            icon = LabelIcon.EMOJI,
                            emoji = event.emoji
                        )
                    }
                }
            }

            is LabelScreenEvent.NotificationStateChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        notificationState = event.notificationState
                    )
                }
            }

            is LabelScreenEvent.ShowEmojiPicker -> {
                _uiState.update { it.copy(emojiPickerIsShowing = true) }
            }

            is LabelScreenEvent.HideEmojiPicker -> {
                _uiState.update { it.copy(emojiPickerIsShowing = false) }
            }

            is LabelScreenEvent.Save -> {
                if (isValid()) save()
            }

            is LabelScreenEvent.Delete -> {
                delete()
            }

            is LabelScreenEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun validateName() {
        val validationResult: ValidationResult =
            validateNameNotEmptyAndDistinct(
                input = _uiState.value.name,
                nameList = allLabels.filter { it.id != labelId }.map { it.name }
            )
        _uiState.update {
            it.copy(nameValidationError = validationResult.getErrorMessage())
        }
    }

    override fun isValid(): Boolean {
        validateName()
        return _uiState.value.nameValidationError == null
    }

    override fun save() {
        viewModelScope.launch {
            val id = _uiState.value.label?.id ?: UUID.randomUUID().toString()
            runCatching {
                labelRepository.upsertLabel(label = createLabelFromUiState(id = id))
            }.onSuccess {
                _editorResultEventFlow.emit(EditorResultEvent.SaveSuccess(id = id))
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error saving tag", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_saving_label)
                )
                _editorResultEventFlow.emit(EditorResultEvent.OperationError(errorMessage))
            }
        }
    }

    private fun createLabelFromUiState(id: String): Label {
        return with(_uiState.value) {
            Label(
                id = id,
                name = name.trim(),
                color = colorId,
                iconId = icon.id,
                notificationState = notificationState,
                emoji = emoji
            )
        }
    }

    fun setNavigationResult(id: String?, isOpenedFromEvent: Boolean) {
        navigationDialogResult.setDialogResultData(
            when {
                isOpenedFromEvent -> DialogResultData.EventLabelResult(id)
                else -> DialogResultData.LabelResult(id)
            }
        )
    }

    override fun delete() {
        viewModelScope.launch {
            runCatching {
                labelRepository.deleteLabelById(_uiState.value.label?.id ?: "")
            }.onSuccess {
                _editorResultEventFlow.emit(EditorResultEvent.DeleteSuccess)
            }.onFailureIfNotCancelled { e ->
                val errorMessage = getLogMessage(logTag, "Error deleting tag", e)
                onError(
                    logMessage = errorMessage,
                    showingMessage = context.resources.getString(R.string.error_deleting_label)
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