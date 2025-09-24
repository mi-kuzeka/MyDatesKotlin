package com.kuzepa.mydates.domain.usecase.label

sealed class LabelsFetching {
    data object Success : LabelsFetching()
    data class Error(val message: String) : LabelsFetching()
}