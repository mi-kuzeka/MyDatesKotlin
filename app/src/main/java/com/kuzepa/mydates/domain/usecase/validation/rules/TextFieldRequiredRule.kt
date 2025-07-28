package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class TextFieldRequiredRule @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider
) {
    fun validate(input: String): ValidationResult {
        return if (input.isBlank()) {
            ValidationResult.Invalid(validationMessageProvider.getEmptyFieldError())
        } else {
            ValidationResult.Valid
        }
    }
}