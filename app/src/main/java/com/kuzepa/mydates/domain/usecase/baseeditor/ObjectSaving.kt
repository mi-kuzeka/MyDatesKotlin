package com.kuzepa.mydates.domain.usecase.baseeditor

sealed class ObjectSaving {
    object Success : ObjectSaving()
    data class Error(val message: String) : ObjectSaving()
}