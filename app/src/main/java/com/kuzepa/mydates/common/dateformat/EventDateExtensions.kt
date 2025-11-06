package com.kuzepa.mydates.common.dateformat

import android.content.Context
import android.icu.util.Calendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuzepa.mydates.common.util.date.getTodayCalendar
import com.kuzepa.mydates.common.util.date.toLocalDate
import com.kuzepa.mydates.domain.formatter.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.formatter.getCalendarWithCurrentYear
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.getEmptyAge

@Composable
fun EventDate.toFormattedDate(showingMode: DateShowingMode): String {
    val dateFormatter = rememberDateFormatter()
    var formattedDate by remember { mutableStateOf("") }

    LaunchedEffect(this) {
        formattedDate =
            dateFormatter.format(
                eventDate = this@toFormattedDate,
                showingMode = showingMode
            )
    }

    return formattedDate
}

fun EventDate.toFormattedDate(context: Context, showingMode: DateShowingMode): String =
    getDateFormatter(context).format(eventDate = this, showingMode = showingMode)

fun EventDate.getAge(currentYear: Int): Int {
    if (year < 0 || currentYear < 0) return getEmptyAge()
    return currentYear - year
}

fun EventDate.isPastEvent(today: Calendar = getTodayCalendar()): Boolean {
    val currentYear = today.toLocalDate().year
    return getCalendarWithCurrentYear(currentYear).timeInMillis < today.timeInMillis
}

fun EventDate.isTodayEvent(today: Calendar = getTodayCalendar()): Boolean {
    val currentYear = today.toLocalDate().year
    return getCalendarWithCurrentYear(currentYear).timeInMillis == today.timeInMillis
}

fun EventDate.isUpcomingEvent(today: Calendar = getTodayCalendar()): Boolean {
    val currentYear = today.toLocalDate().year
    return getCalendarWithCurrentYear(currentYear).timeInMillis > today.timeInMillis
}
