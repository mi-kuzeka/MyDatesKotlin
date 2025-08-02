package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.domain.converter.isDateValid
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationResult
import javax.inject.Inject

class DateFormatRule @Inject constructor(
    private val validationMessageProvider: ValidationMessageProvider,
    private val dateFormatProvider: DateFormatProvider
) {
    fun validate(input: String, hideYear: Boolean): ValidationResult {
        if (input.isBlank()) {
            return ValidationResult.Invalid(
                validationMessageProvider.getEmptyFieldError()
            )
        }
        val eventDate = dateFormatProvider.getEditedEventDate(input, hideYear)
        return if (isDateValid(eventDate)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                validationMessageProvider.getWrongDateFormatError()
            )
        }
    }
}