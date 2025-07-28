package com.kuzepa.mydates.domain.model

enum class DateFormatLength(numberCount: Int) {
    NO_YEAR(numberCount = 4),
    FULL(numberCount = 8)
}