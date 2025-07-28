package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class SelectionRequiredRule @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider
) {
    fun validate(isSelected: Boolean): ValidationResult {
        return if (isSelected) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(validationMessageProvider.getSelectionRequiredError())
        }
    }
}