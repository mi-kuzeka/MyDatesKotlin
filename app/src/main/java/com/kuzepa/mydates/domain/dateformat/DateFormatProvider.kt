package com.kuzepa.mydates.domain.dateformat

import com.kuzepa.mydates.domain.model.EventDate

interface DateFormatProvider {
    fun getShowingMask(hideYear: Boolean): String {
        return if (hideYear) getShortMask() else getFullMask()
    }
    fun getFullMask(): String
    fun getShortMask(): String
    fun getDelimiter(): Char
    fun getFormattingPattern(hideYear: Boolean): String {
        return if (hideYear) getShortFormattingPattern() else getFullFormattingPattern()
    }

    fun getFullFormattingPattern(): String
    fun getShortFormattingPattern(): String
    fun getFormattedDate(eventDate: EventDate, showingMode: DateShowingMode): String
    fun getEditedEventDate(formattedDate: String, hideYear: Boolean): EventDate?
    fun dateIsFilled(inputDate: String, hideYear: Boolean): Boolean
    fun getDateWithoutYear(inputDate: String): String
}