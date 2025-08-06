package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class ValidateTextNotEmptyUseCase @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider
) {
    operator fun invoke(input: String): ValidationResult {
        return if (input.isBlank()) {
            ValidationResult.Invalid(validationMessageProvider.getEmptyFieldError())
        } else {
            ValidationResult.Valid
        }
    }
}