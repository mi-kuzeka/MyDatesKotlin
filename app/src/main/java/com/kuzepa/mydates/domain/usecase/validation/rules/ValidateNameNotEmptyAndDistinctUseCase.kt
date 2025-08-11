package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class ValidateNameNotEmptyAndDistinctUseCase @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider
) {
    operator fun invoke(input: String, nameList: List<String>): ValidationResult {
        if (input.isBlank()) {
            return ValidationResult.Invalid(validationMessageProvider.getEmptyFieldError())
        }
        if (nameList.isEmpty()) return ValidationResult.Valid

        return if (nameList.any { name -> name.equals(input, ignoreCase = true) }) {
            ValidationResult.Invalid(validationMessageProvider.getFiledIsNotDistinctError())
        } else {
            ValidationResult.Valid
        }
    }
}