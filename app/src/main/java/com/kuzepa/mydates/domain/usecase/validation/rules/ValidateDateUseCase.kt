package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.formatter.isDateValid
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class ValidateDateUseCase @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider,
    private val dateFormatProvider: DateFormatProvider
) {
    operator fun invoke(input: String, hideYear: Boolean): ValidationResult {
        if (input.isBlank()) {
            return ValidationResult.Invalid(
                validationMessageProvider.getEmptyFieldError()
            )
        }
        val eventDate = dateFormatProvider.getEditedEventDate(input, hideYear)
        return if (eventDate.isDateValid()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                validationMessageProvider.getWrongDateFormatError(
                    dateFormatProvider.getShowingMask(hideYear)
                )
            )
        }
    }
}