package com.kuzepa.mydates.domain.usecase.validation

interface ValidationMessageProvider {
    fun getEmptyFieldError(): String
    fun getWrongDateFormatError(requiredFormat: String): String
    fun getSelectionRequiredError(): String
    fun getFieldIsNotDistinctError(): String
}