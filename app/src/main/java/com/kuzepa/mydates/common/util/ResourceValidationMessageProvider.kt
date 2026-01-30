package com.kuzepa.mydates.common.util

import android.content.Context
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider

class ResourceValidationMessageProvider(
    private val context: Context
) : ValidationMessageProvider {
    override fun getEmptyFieldError(): String =
        context.resources.getString(R.string.error_field_is_required)

    override fun getWrongDateFormatError(requiredFormat: String): String =
        context.resources.getString(R.string.error_incorrect_date_format_pattern, requiredFormat)

    override fun getShortDateLengthError(requiredLength: Int): String =
        context.resources.getString(R.string.error_short_date_pattern, "$requiredLength")

    override fun getSelectionRequiredError(): String {
        TODO("Not yet implemented")
    }

    override fun getFieldIsNotDistinctError(): String =
        context.resources.getString(R.string.error_name_is_duplicated)
}