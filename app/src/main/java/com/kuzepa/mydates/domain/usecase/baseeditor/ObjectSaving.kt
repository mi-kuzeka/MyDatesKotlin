package com.kuzepa.mydates.domain.usecase.baseeditor

sealed class ObjectSaving {
    data class Success(val id: String? = null) : ObjectSaving()
}