package com.kuzepa.mydates.domain.dateformat

import com.kuzepa.mydates.domain.model.EventDate

interface DateFormatProvider {
    fun getShowingMask(hideYear: Boolean): String
    fun getDelimiter(): Char
    fun getFormattingPattern(hideYear: Boolean): String
    fun getFormattedDate(eventDate: EventDate, showingMode: DateShowingMode): String
    fun getEditedEventDate(formattedDate: String, hideYear: Boolean): EventDate?
    fun dateIsFilled(inputDate: String, hideYear: Boolean): Boolean
}