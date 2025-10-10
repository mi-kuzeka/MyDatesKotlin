package com.kuzepa.mydates.ui.components.baseeditor

import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.ui.components.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseEditorViewModel<UI_STATE, SCREEN_EVENT> : BaseViewModel<SCREEN_EVENT>() {
    abstract val uiState: StateFlow<UI_STATE>
    abstract val savingFlow: Flow<ObjectSaving>
    abstract val deletingFlow: Flow<ObjectDeleting>

    protected abstract fun isValid(): Boolean
    protected abstract fun save()
    protected abstract fun delete()
}