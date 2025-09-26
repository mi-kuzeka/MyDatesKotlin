package com.kuzepa.mydates.feature.more.label.labelchooser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.ui.navigation.NavigationResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LabelChooserViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val eventLabelIdsJson: String? by lazy { savedStateHandle.get<String>("eventLabelIdsJson") }
    private val _uiState = MutableStateFlow(LabelChooserUiState())
    val uiState: StateFlow<LabelChooserUiState> = _uiState.asStateFlow()

    private val savingLabelChooserChannel = Channel<ObjectSaving>()
    val savingFlow: Flow<ObjectSaving> = savingLabelChooserChannel.receiveAsFlow()

    init {
        loadLabels()
    }

    private fun loadLabels() {
        val eventLabelIdList = eventLabelIdsJson?.let { json ->
            try {
                Json.decodeFromString<List<String>>(json)
            } catch (_: Exception) {
                listOf()
            }
        } ?: listOf()
        viewModelScope.launch {
            try {
                labelRepository.getAllLabels().collect { labels ->
                    val availableLabels = if (eventLabelIdList.isEmpty()) {
                        labels
                    } else {
                        labels.filter { it.id !in eventLabelIdList }
                    }
                    _uiState.update {
                        it.copy(
                            labels = availableLabels,
                            selectedLabel = availableLabels.firstOrNull()
                        )
                    }
                }
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    fun onEvent(event: LabelChooserScreenEvent) {
        when (event) {
            is LabelChooserScreenEvent.LabelSelected -> {
                _uiState.update {
                    it.copy(
                        selectedLabel = event.label
                    )
                }
            }

            is LabelChooserScreenEvent.OnLabelNavigationResult -> {
                handleLabelResult(event.navigationResult)
            }

            is LabelChooserScreenEvent.Save -> {
                viewModelScope.launch {
                    savingLabelChooserChannel.send(
                        ObjectSaving.Success(id = _uiState.value.selectedLabel?.id)
                    )
                }
            }
        }
    }

    private fun handleLabelResult(result: NavigationResultData) {
        result.id?.let { id ->
            val label: Label? = _uiState.value.labels.firstOrNull { it.id == id }
            label?.let {
                updateLabel(it)
            } ?: {
                viewModelScope.launch {
                    try {
                        val newLabel = labelRepository.getLabelById(id)
                        newLabel?.let { addLabel(it) }
                    } catch (e: Exception) {
                        // TODO handle error
                    }
                }
            }
        }
    }

    private fun addLabel(label: Label) {
        _uiState.update {
            it.copy(
                labels = it.labels + label,
                selectedLabel = label
            )
        }
    }

    private fun updateLabel(newLabel: Label) {
        _uiState.update {
            it.copy(
                labels = it.labels.map { oldLabel ->
                    if (oldLabel.id == newLabel.id) newLabel else oldLabel
                },
                selectedLabel = newLabel
            )
        }
    }
}