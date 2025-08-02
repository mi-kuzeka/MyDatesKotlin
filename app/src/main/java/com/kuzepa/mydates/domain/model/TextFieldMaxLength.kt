package com.kuzepa.mydates.domain.model

enum class TextFieldMaxLength(val length: Int) {
    NAME(length = 100),
    NOTES(length = 1000),
    LABEL_NAME(length = 20)
}