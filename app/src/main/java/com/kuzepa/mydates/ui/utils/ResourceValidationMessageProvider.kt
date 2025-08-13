package com.kuzepa.mydates.ui.utils

import android.content.Context
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider

class ResourceValidationMessageProvider(
    private val context: Context
) : ValidationMessageProvider {
    override fun getEmptyFieldError(): String =
        context.resources.getString(R.string.error_field_is_required)

    override fun getWrongDateFormatError(requiredFormat: String): String =
        context.resources.getString(R.string.error_date_incorrect_format, requiredFormat)

    override fun getSelectionRequiredError(): String {
        TODO("Not yet implemented")
    }

    override fun getFiledIsNotDistinctError(): String {
        TODO("Not yet implemented")
    }
}