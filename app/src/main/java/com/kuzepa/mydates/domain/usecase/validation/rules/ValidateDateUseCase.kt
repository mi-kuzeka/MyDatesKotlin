package com.kuzepa.mydates.domain.usecase.validation.rules

import com.kuzepa.mydates.common.dateformat.DateFormatterResult
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.formatter.isDateValid
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
        return when (val result = dateFormatProvider.getEditedEventDate(input, hideYear)) {
            is DateFormatterResult.ShortLength -> {
                ValidationResult.Invalid(
                    errorMessage = validationMessageProvider.getShortDateLengthError(
                        requiredLength = result.limit
                    )
                )
            }

            is DateFormatterResult.Error -> {
                val requiredFormat = dateFormatProvider.getShowingMask(hideYear)
                ValidationResult.Invalid(
                    errorMessage = validationMessageProvider.getWrongDateFormatError(
                        requiredFormat = requiredFormat
                    ),
                    logMessage = getLogMessage(
                        tag = "ValidateDate",
                        title = "Error getting event date from string: '$input', required format: '$requiredFormat'",
                        throwable = result.e
                    )
                )
            }

            is DateFormatterResult.OK -> {
                if (result.eventDate.isDateValid()) {
                    ValidationResult.Valid
                } else {
                    val requiredFormat = dateFormatProvider.getShowingMask(hideYear)
                    ValidationResult.Invalid(
                        errorMessage = validationMessageProvider.getWrongDateFormatError(
                            requiredFormat = dateFormatProvider.getShowingMask(hideYear)
                        ),
                        logMessage = getLogMessage(
                            tag = "ValidateDate",
                            title = "Error getting event date from string: '$input', required format: '$requiredFormat'",
                            message = "Wrong date format"
                        )
                    )
                }
            }
        }
    }
}