package com.kuzepa.mydates.domain.converter

import com.kuzepa.mydates.domain.dateformat.DateField
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.hasYear

fun EventDate.getNotificationDateCode(): Int {
    return month * 100 + day
}

fun EventDate.formatDate(formatPattern: String, delimiter: Char): String {
    val formatGroups = formatPattern.split(delimiter)
    val groupCounts = formatGroups.map { it.length }
    val formatString: String =
        groupCounts.mapIndexed { index, count ->
            if (index == groupCounts.lastIndex) {
                // Don't add a delimiter to the end of string
                "%0${count}d"
            } else {
                "%0${count}d$delimiter"
            }
        }.joinToString(separator = "")
    return formatString.format(month, day, year)
}

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

fun isDateValid(eventDate: EventDate?): Boolean {
    if (eventDate == null) return false
    with(eventDate) {
        if (month !in 1..12) return false
        if (day !in 1..31) return false
        return when (month) {
            2 -> {
                if (day <= 28) true
                if (day > 29) false
                // only day=29 is left at this step
                if (hasYear()) {
                    return isLeapYear(year)
                } else {
                    true
                }
            }

            4, 6, 9, 11 -> day <= 30

            else -> true
        }
    }
}

private fun isLeapYear(year: Int): Boolean =
    ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))