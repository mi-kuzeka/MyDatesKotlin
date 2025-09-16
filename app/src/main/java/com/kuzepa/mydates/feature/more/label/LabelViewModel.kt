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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    private val validateNameNotEmptyAndDistinct: ValidateNameNotEmptyAndDistinctUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEditorViewModel<LabelUiState, LabelScreenEvent>() {

    private val labelId: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(LabelUiState())
    override val uiState: StateFlow<LabelUiState> = _uiState.asStateFlow()

    /**
     * Required to check if the name is duplicated
     */
    private var allLabels: List<Label> = emptyList()

    private val savingLabelChannel = Channel<ObjectSaving>()
    override val savingFlow = savingLabelChannel.receiveAsFlow()

    private val deletingLabelChannel = Channel<ObjectDeleting>()
    override val deletingFlow = deletingLabelChannel.receiveAsFlow()


    init {
        _uiState.value = _uiState.value.copy(isNewLabel = labelId == null)
        loadLabels()
    }

    private fun loadLabels() {
        viewModelScope.launch {
            try {
                labelRepository.getAllLabels().collect { labels ->
                    allLabels = labels
                    if (!_uiState.value.isNewLabel && allLabels.isNotEmpty()) {
                        allLabels.firstOrNull { it.id == labelId }?.populateScreenFields()
                    }
                }
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    private fun Label.populateScreenFields() {
        _uiState.value.copy(
            label = this,
            name = name,
            colorId = color,
            icon = LabelIcon.fromId(iconId) ?: LabelIcon.FIRST_LETTER,
            notificationState = notificationState
        )
    }

    override fun onEvent(event: LabelScreenEvent) {
        when (event) {
            is LabelScreenEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    name = event.name,
                    nameFirstLetter = event.name.take(1)
                )
                validateName()
            }

            is LabelScreenEvent.ColorChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    colorId = event.color
                )
            }

            is LabelScreenEvent.IconChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    icon = event.icon
                )
            }

            is LabelScreenEvent.NotificationStateChanged -> {
                _uiState.value = _uiState.value.copy(
                    hasChanges = true,
                    notificationState = event.notificationState
                )
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
                nameList = allLabels.map { it.name }
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
                savingLabelChannel.send(ObjectSaving.Success(id = id))
            } catch (e: Exception) {
                // TODO handle error
                savingLabelChannel.send(ObjectSaving.Error(e.message.toString()))
            }
        }
    }

    override fun delete() {
        viewModelScope.launch() {
            try {
                labelRepository.deleteLabelById(_uiState.value.label?.id ?: "")
                deletingLabelChannel.send(ObjectDeleting.Success)
            } catch (e: Exception) {
                // TODO handle error
                deletingLabelChannel.send(ObjectDeleting.Error(e.message.toString()))
            }
        }
    }
}