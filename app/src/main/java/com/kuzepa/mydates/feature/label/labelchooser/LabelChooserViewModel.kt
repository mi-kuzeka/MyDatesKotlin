package com.kuzepa.mydates.feature.label.labelchooser

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.ui.components.BaseViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LabelChooserViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val labelRepository: LabelRepository,
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<LabelChooserScreenEvent>() {
    private val eventLabelIdsJson: String? by lazy { savedStateHandle.get<String>("eventLabelIdsJson") }
    private val _uiState = MutableStateFlow(LabelChooserUiState())
    val uiState: StateFlow<LabelChooserUiState> = _uiState.asStateFlow()

    private val savingLabelChooserSharedFlow = MutableSharedFlow<ObjectSaving>(replay = 0)
    val savingFlow = savingLabelChooserSharedFlow.asSharedFlow()

    private val logTag = "LabelChooserViewModel"

    init {
        loadLabels()
        observeDialogResults()
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
            labelRepository.getAllLabels()
                .catch { e ->
                    if (e is CancellationException) throw e
                    onError(
                        logMessage = getLogMessage(logTag, "Error getting labels", e),
                        showingMessage = context.resources.getString(R.string.error_getting_labels)
                    )
                }
                .collect { labels ->
                    val availableLabels = if (eventLabelIdList.isEmpty()) {
                        labels
                    } else {
                        labels.filter { it.id !in eventLabelIdList }
                    }
                    val selectedLabel: Label? = _uiState.value.selectedLabelId?.let { id ->
                        availableLabels.firstOrNull { it.id == id }
                    } ?: availableLabels.firstOrNull()

                    _uiState.update {
                        it.copy(
                            labels = availableLabels,
                            selectedLabel = selectedLabel,
                            selectedLabelId = selectedLabel?.id
                        )
                    }
                }
        }
    }

    private fun observeDialogResults() {
        viewModelScope.launch {
            navigationDialogResult.dialogResultData.collect { result ->
                when (result) {
                    is DialogResultData.LabelResult -> {
                        handleLabelResult(result.id)
                        navigationDialogResult.clearDialogResultData()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onEvent(event: LabelChooserScreenEvent) {
        when (event) {
            is LabelChooserScreenEvent.LabelSelected -> {
                _uiState.update {
                    it.copy(
                        selectedLabel = event.label,
                        selectedLabelId = event.label.id
                    )
                }
            }

            is LabelChooserScreenEvent.Save -> {
                val id = _uiState.value.selectedLabelId
                navigationDialogResult.setDialogResultData(DialogResultData.EventLabelResult(id))
                viewModelScope.launch {
                    savingLabelChooserSharedFlow.emit(ObjectSaving.Success(id = id))
                }
            }

            is LabelChooserScreenEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun handleLabelResult(labelId: String?) {
        labelId?.let { id ->
            _uiState.update { it.copy(selectedLabelId = id) }
        }
    }

    suspend fun onError(logMessage: String, showingMessage: String) {
        withContext((Dispatchers.IO)) {
            errorLoggerRepository.logError(logMessage)
        }
        withContext(Dispatchers.Main) {
            setError(showingMessage)
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}