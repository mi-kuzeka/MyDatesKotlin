package com.kuzepa.mydates.domain.dateformat

import com.kuzepa.mydates.domain.model.EventDate

interface DateFormatProvider {
    fun getShowingMask(showYear: Boolean): String
    fun getDelimiter(): Char
    fun getFormattingPattern(showYear: Boolean): String
    fun getFormattedDate(eventDate: EventDate, showingMode: DateShowingMode): String
    fun getEditedEventDate(formattedDate: String, showYear: Boolean): EventDate?
}