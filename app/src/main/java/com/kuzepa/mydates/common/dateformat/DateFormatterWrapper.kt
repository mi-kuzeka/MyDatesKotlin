package com.kuzepa.mydates.common.dateformat

import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.formatter.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.EventDate
import javax.inject.Inject

class DateFormatterWrapper @Inject constructor(
    private val dateFormatter: DateFormatProvider
) {
    fun format(eventDate: EventDate, showingMode: DateShowingMode): String {
        return dateFormatter.getFormattedDate(eventDate, showingMode)
    }
}