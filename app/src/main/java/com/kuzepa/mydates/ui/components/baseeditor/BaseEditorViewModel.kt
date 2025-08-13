package com.kuzepa.mydates.ui.components.baseeditor

import androidx.lifecycle.ViewModel
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseEditorViewModel<UI_STATE, SCREEN_EVENT> : ViewModel() {
    abstract val uiState: StateFlow<UI_STATE>
    abstract val savingFlow: Flow<ObjectSaving>
    abstract val deletingFlow: Flow<ObjectDeleting>

    abstract fun onEvent(event: SCREEN_EVENT)

    protected abstract fun isValid(): Boolean
    protected abstract fun save()
    protected abstract fun delete()
}