package com.kuzepa.mydates.ui.common

import android.content.Context
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider

class ResourceValidationMessageProvider(
    private val context: Context
) : ValidationMessageProvider {
    // e.g.
    //override fun getEmptyFieldError() =
    //        context.getString(R.string.validation_empty_field)
    override fun getEmptyFieldError(): String {
        TODO("Not yet implemented")
    }

    override fun getWrongDateFormatError(): String {
        TODO("Not yet implemented")
    }

    override fun getSelectionRequiredError(): String {
        TODO("Not yet implemented")
    }

    override fun getFiledIsNotDistinct(): String {
        TODO("Not yet implemented")
    }
}