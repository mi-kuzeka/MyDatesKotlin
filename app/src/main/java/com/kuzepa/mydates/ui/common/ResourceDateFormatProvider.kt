package com.kuzepa.mydates.ui.common

import android.content.Context
import com.kuzepa.mydates.domain.converter.formatDate
import com.kuzepa.mydates.domain.converter.getEventDateFromFormattedDateWithoutDelimiter
import com.kuzepa.mydates.domain.dateformat.DateField
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.EventDate

class ResourceDateFormatProvider(
    private val context: Context
) : DateFormatProvider {

    override fun getShowingMask(showYear: Boolean): String {
        return "mm/dd/yyyy"
        //TODO get from datastore
//        return if (showYear) {
//            context.resources.getString()
//        }
    }

    override fun getDelimiter(): Char {
        return '/'
        //TODO get from datastore
    }

    override fun getFormattingPattern(showYear: Boolean): String {
        return "MMddyyyy"
        //TODO get from datastore
    }

    override fun getFormattedDate(
        eventDate: EventDate,
        showingMode: DateShowingMode
    ): String {
        val delimiter = getDelimiter()
        val formatPattern = when (showingMode) {
            DateShowingMode.EDIT_MODE -> {
                // TODO set actual formatPattern
                "mm/dd/yyyy"
            }

            DateShowingMode.VIEW_MODE -> {
                // TODO set actual formatPattern
                "MMM d, yyyy"
            }

            DateShowingMode.REMINDER_MODE -> {
                // TODO set actual formatPattern
                "MMMM d"
            }
        }
        return eventDate.formatDate(formatPattern, delimiter)
    }

    override fun getEditedEventDate(
        formattedDate: String,
        showYear: Boolean
    ): EventDate? {
        //TODO get from datastore
        val dateFieldOrder: Array<DateField> =
            arrayOf(DateField.MONTH, DateField.DAY, DateField.YEAR)
        val delimiter = getDelimiter()
        return getEventDateFromFormattedDateWithoutDelimiter(
            dateFieldOrder = dateFieldOrder,
            showYear = showYear,
            formattedDate = formattedDate
        )
    }
}