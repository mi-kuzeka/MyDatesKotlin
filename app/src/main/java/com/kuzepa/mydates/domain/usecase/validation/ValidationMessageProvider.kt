package com.kuzepa.mydates.domain.usecase.validation

interface ValidationMessageProvider {
    fun getEmptyFieldError(): String
    fun getWrongDateFormatError(): String
    fun getSelectionRequiredError(): String
    fun getFiledIsNotDistinct(): String
}