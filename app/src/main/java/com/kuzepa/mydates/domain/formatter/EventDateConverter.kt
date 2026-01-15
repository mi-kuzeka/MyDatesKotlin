package com.kuzepa.mydates.domain.formatter

import android.icu.util.Calendar
import com.kuzepa.mydates.domain.formatter.dateformat.DateField
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.hasYear
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Converts event date to an Int with format Mdd
 * E.g.:
 *  EventDate(month = 3, day = 8, year = 2000) -> result = 308
 *  EventDate(month = 10, day = 21, year = -1) -> result = 1021
 */
fun EventDate.toDateCode(): Int {
    return month * 100 + day
}

fun Calendar.toDateCode(): Int {
    return (get(Calendar.MONTH) + 1) * 100 + get(Calendar.DAY_OF_MONTH)
}

fun EventDate.formatDate(formatPattern: String): String {
    val calendar = Calendar.getInstance().apply {
        if (hasYear()) set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
    }
    return SimpleDateFormat(formatPattern, Locale.getDefault()).format(calendar.time)
}

fun EventDate.toEditedDateString(
    dateFieldOrder: Array<DateField>,
): String {
    var result = ""
    for (dateField in dateFieldOrder) {
        when (dateField) {
            DateField.MONTH -> {
                result += month.toFixedLengthString(length = 2)
            }

            DateField.DAY -> {
                result += day.toFixedLengthString(length = 2)
            }

            DateField.YEAR -> {
                if (hasYear()) result += year
            }
        }
    }
    return result
}

/**
 * Converts formatted date to [EventDate]
 * @param dateFieldOrder defines the order of month, day and year in the formatted string
 * @param hideYear if true then format is only with month and day, without year
 * @param formattedDate date string without delimiters, e.g.: MMddyyyy, ddMM, etc
 * @return [EventDate] or null
 */
fun getEventDateFromFormattedDateWithoutDelimiter(
    dateFieldOrder: Array<DateField>,
    hideYear: Boolean,
    formattedDate: String
): EventDate? {
    if (hideYear) {
        if (formattedDate.length < 4) return null
    } else {
        if (formattedDate.length < 8) return null
    }

    var month = ""
    var day = ""
    var year = ""
    var startIndex = 0
    var endIndex = 0
    for (dateField in dateFieldOrder) {
        when (dateField) {
            DateField.MONTH -> {
                val monthSymbolsCount = 2
                endIndex = startIndex + monthSymbolsCount
                month = formattedDate.substring(startIndex, endIndex)
            }

            DateField.DAY -> {
                val daySymbolsCount = 2
                endIndex = startIndex + daySymbolsCount
                day = formattedDate.substring(startIndex, endIndex)
            }

            DateField.YEAR -> {
                if (!hideYear) {
                    val yearSymbolsCount = 4
                    endIndex = startIndex + yearSymbolsCount
                    year = formattedDate.substring(startIndex, endIndex)
                }
            }
        }
        startIndex = endIndex
    }

    return try {
        EventDate(
            month = month.toInt(),
            day = day.toInt(),
            year = if (hideYear) -1 else year.toInt()
        )
    } catch (e: NumberFormatException) {
        // TODO handle exception
        null
    }
}

fun EventDate?.isDateValid(): Boolean {
    if (this == null) return false
    if (month !in 1..12) return false
    if (day !in 1..31) return false
    when (month) {
        2 -> {
            if (day <= 28) return true
            if (day > 29) return false
            // only day=29 is left at this step
            return if (hasYear()) {
                isLeapYear(year)
            } else {
                true
            }
        }

        4, 6, 9, 11 -> return day <= 30

        else -> return true
    }
}

private fun isLeapYear(year: Int): Boolean =
    ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))

fun EventDate.getCalendarWithCurrentYear(currentYear: Int): Calendar =
    Calendar.getInstance().apply {
        set(Calendar.YEAR, currentYear)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }