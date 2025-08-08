package com.kuzepa.mydates.ui.common.utils.dateformat

import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.EventDate
import javax.inject.Inject

class DateFormatterWrapper @Inject constructor(
    private val dateFormatter: DateFormatProvider
) {
    fun format(eventDate: EventDate, showingMode: DateShowingMode): String {
        return dateFormatter.getFormattedDate(eventDate, showingMode)
    }
}