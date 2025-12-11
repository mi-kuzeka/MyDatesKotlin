package com.kuzepa.mydates.domain.usecase.baseeditor

sealed class EditorResultEvent {
    data class SaveSuccess(val id: String? = null) : EditorResultEvent()
    data object DeleteSuccess : EditorResultEvent()
    data class OperationError(val message: String) : EditorResultEvent()
}