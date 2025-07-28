package com.kuzepa.mydates.domain.usecase.validation

sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Invalid(val errorMessage: String) : ValidationResult
}