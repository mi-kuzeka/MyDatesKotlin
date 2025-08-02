package com.kuzepa.mydates.domain.usecase.validation

import com.kuzepa.mydates.domain.usecase.validation.ValidationResult.Invalid
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult.Valid

sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Invalid(val errorMessage: String) : ValidationResult
}

fun ValidationResult.getErrorMessage(): String? =
    when (this) {
        is Valid -> null
        is Invalid -> errorMessage
    }