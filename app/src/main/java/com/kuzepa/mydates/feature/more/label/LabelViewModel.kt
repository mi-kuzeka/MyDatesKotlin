package com.kuzepa.mydates.feature.more.label

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import com.kuzepa.mydates.domain.usecase.validation.getErrorMessage
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<LabelUiState, LabelScreenEvent>() {

    private val labelId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(LabelUiState())
    override val uiState: StateFlow<LabelUiState> = _uiState.asStateFlow()

    /**
     * Required to check if the name is duplicated
     */
    private var allLabels: List<Label> = emptyList()

    private val savingLabelSharedFlow = MutableSharedFlow<ObjectSaving>()
    override val savingFlow = savingLabelSharedFlow.asSharedFlow()

    private val deletingLabelSharedFlow = MutableSharedFlow<ObjectDeleting>()
    override val deletingFlow = deletingLabelSharedFlow.asSharedFlow()

    init {
        _uiState.update { it.copy(isNewLabel = labelId == null) }
        loadLabels()
        observeDialogResults()
    }

    private fun loadLabels() {
        viewModelScope.launch {
            try {
                allLabels = labelRepository.getAllLabels().firstOrNull() ?: emptyList()
                if (!_uiState.value.isNewLabel && allLabels.isNotEmpty()) {
                    allLabels.firstOrNull { it.id == labelId }?.populateScreenFields()
                }
            } catch (e: Exception) {
                // TODO handle error
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

            is LabelScreenEvent.NotificationStateChanged -> {
                _uiState.update {
                    it.copy(
                        hasChanges = true,
                        notificationState = event.notificationState
                    )
                }
            }

            is LabelScreenEvent.Save -> {
                if (isValid()) save()
            }

            is LabelScreenEvent.Delete -> {
                delete()
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
                val id = _uiState.value.label?.id ?: UUID.randomUUID().toString()
                with(_uiState.value) {
                    labelRepository.upsertLabel(
                        Label(
                            id = id,
                            name = name,
                            color = colorId,
                            iconId = icon.id,
                            notificationState = notificationState
                        )
                    )
                }
                savingLabelSharedFlow.emit(ObjectSaving.Success(id = id))
            } catch (e: Exception) {
                // TODO handle error
                savingLabelSharedFlow.emit(ObjectSaving.Error(e.message.toString()))
            }
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
        viewModelScope.launch() {
            try {
                labelRepository.deleteLabelById(_uiState.value.label?.id ?: "")
                deletingLabelSharedFlow.emit(ObjectDeleting.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingLabelSharedFlow.emit(ObjectDeleting.Error(e.message.toString()))
            }
        }
    }
}