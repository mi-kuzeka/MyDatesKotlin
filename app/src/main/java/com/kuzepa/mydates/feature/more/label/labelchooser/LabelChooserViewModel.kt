package com.kuzepa.mydates.feature.more.label.labelchooser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.repository.LabelRepository
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelChooserViewModel @Inject constructor(
    private val labelRepository: LabelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LabelChooserUiState())
    val uiState: StateFlow<LabelChooserUiState> = _uiState.asStateFlow()

    private val savingLabelChooserChannel = Channel<ObjectSaving>()
    val savingFlow: Flow<ObjectSaving> = savingLabelChooserChannel.receiveAsFlow()

    init {
        loadLabels()
    }

    private fun loadLabels() {
        viewModelScope.launch {
            try {
                labelRepository.getAllLabels().collect { labels ->
                    _uiState.update {
                        it.copy(
                            labels = labels,
                            selectedLabel = labels.firstOrNull()
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

            }
            is LabelChooserScreenEvent.LabelUpdated -> {

            }
            is LabelChooserScreenEvent.NewLabelAdded -> {

            }
            is LabelChooserScreenEvent.Save -> {

            }
        }
    }

    fun isValid(): Boolean {
        TODO("Not yet implemented")
    }

    fun save() {
        TODO("Not yet implemented")
    }
}