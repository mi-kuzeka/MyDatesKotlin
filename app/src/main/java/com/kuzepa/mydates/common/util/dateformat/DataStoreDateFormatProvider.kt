package com.kuzepa.mydates.common.util.dateformat

import android.content.Context
import com.kuzepa.mydates.domain.formatter.dateformat.DateField
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.formatter.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.formatter.formatDate
import com.kuzepa.mydates.domain.formatter.getEventDateFromFormattedDateWithoutDelimiter
import com.kuzepa.mydates.domain.formatter.toEditedDateString
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.hasYear

class DataStoreDateFormatProvider(
    private val context: Context
) : DateFormatProvider {
    override fun getFullMask(): String {
        return "mm/dd/yyyy"
        //TODO get from datastore
    }

    override fun getShortMask(): String {
        return "mm/dd"
        //TODO get from datastore
    }

    override fun getDelimiter(): Char {
        return '/'
        //TODO get from datastore
    }

    override fun getFullFormattingPattern(): String {
        return "MMddyyyy"
        //TODO get from datastore
    }

    override fun getShortFormattingPattern(): String {
        return "MMdd"
        //TODO get from datastore
    }

    override fun getFormattedDate(eventDate: EventDate, showingMode: DateShowingMode): String {
        val formatPattern = when (showingMode) {
            DateShowingMode.VIEW_MODE -> {
                // TODO set actual formatPattern
                if (eventDate.hasYear()) {
                    "MMM d, yyyy"
                } else {
                    "MMM d"
                }
            }

            DateShowingMode.REMINDER_MODE -> {
                // TODO set actual formatPattern
                "MMMM d"
            }
        }
        return eventDate.formatDate(formatPattern)
    }

    override fun getEditedEventDate(
        formattedDate: String,
        hideYear: Boolean
    ): EventDate? {
        //TODO get from datastore
        val dateFieldOrder: Array<DateField> =
            arrayOf(DateField.MONTH, DateField.DAY, DateField.YEAR)
        return getEventDateFromFormattedDateWithoutDelimiter(
            dateFieldOrder = dateFieldOrder,
            hideYear = hideYear,
            formattedDate = formattedDate
        )
    }

    override fun getEditedDateString(eventDate: EventDate): String {
        //TODO get from datastore
        val dateFieldOrder: Array<DateField> =
            arrayOf(DateField.MONTH, DateField.DAY, DateField.YEAR)
        return eventDate.toEditedDateString(dateFieldOrder)
    }

    override fun dateIsFilled(inputDate: String, hideYear: Boolean): Boolean {
        return getFormattingPattern(hideYear).length == inputDate.length
    }

    override fun getDateWithoutYear(inputDate: String): String {
        return inputDate.take(getShortFormattingPattern().length)
    }
}