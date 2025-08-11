package com.kuzepa.mydates.domain.usecase.baseeditor

sealed class ObjectDeleting {
    object Success : ObjectDeleting()
    data class Error(val message: String) : ObjectDeleting()
}