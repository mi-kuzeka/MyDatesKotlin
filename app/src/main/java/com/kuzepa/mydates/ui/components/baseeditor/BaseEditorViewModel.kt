package com.kuzepa.mydates.ui.components.baseeditor

import com.kuzepa.mydates.domain.usecase.baseeditor.EditorResultEvent
import com.kuzepa.mydates.ui.components.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseEditorViewModel<UI_STATE, SCREEN_EVENT> : BaseViewModel<SCREEN_EVENT>() {
    abstract val uiState: StateFlow<UI_STATE>
    abstract val editorResultEventFlow: SharedFlow<EditorResultEvent>

    protected abstract fun isValid(): Boolean
    protected abstract fun save()
    protected abstract fun delete()
}