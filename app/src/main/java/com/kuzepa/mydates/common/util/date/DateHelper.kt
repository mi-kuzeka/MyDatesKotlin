package com.kuzepa.mydates.common.util.date

import android.icu.util.Calendar
import android.icu.util.TimeZone
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

fun getTodayCalendar(): Calendar = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.toLocalDate(): LocalDate {
    val jodaTimeZone = try {
        DateTimeZone.forID(timeZone.id)
    } catch (_: IllegalArgumentException) {
        DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZone.id) as java.util.TimeZone?)
    }
    return DateTime(timeInMillis, jodaTimeZone).toLocalDate()
}