package com.kuzepa.mydates.domain.formatter.dateformat

import java.util.Objects

data class FullDateFormat(
    val dateFieldOrder: Array<DateField> = arrayOf(DateField.MONTH, DateField.DAY, DateField.YEAR),
    val dateDelimiter: DateFormatPattern.Delimiter
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullDateFormat

        if (!dateFieldOrder.contentEquals(other.dateFieldOrder)) return false
        if (dateDelimiter != other.dateDelimiter) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(dateFieldOrder, dateDelimiter)
    }
}