package com.kuzepa.mydates.feature.eventlist

import android.content.Context
import android.icu.util.Calendar
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.dateformat.getAge
import com.kuzepa.mydates.common.dateformat.isPastEvent
import com.kuzepa.mydates.common.dateformat.isUpcomingEvent
import com.kuzepa.mydates.common.dateformat.toFormattedDate
import com.kuzepa.mydates.common.util.date.getTodayCalendar
import com.kuzepa.mydates.common.util.date.toLocalDate
import com.kuzepa.mydates.domain.formatter.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.formatter.getCalendarWithCurrentYear
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.ZodiacSign
import org.joda.time.Days

abstract class EventListItem {
    abstract val id: Long
}

data class EventListDelimiter(
    override val id: Long,
    val title: String,
) : EventListItem()

data class EventItemData(
    override val id: Long,
    val event: Event,
    val formattedDate: String,
    val formattedAge: String?,
    val showTurnsWord: Boolean,
    val daysToEventText: String,
    val isUpcoming: Boolean,
    val zodiac: ZodiacSign?
) : EventListItem() {
    companion object {
        fun fromEvent(
            event: Event,
            context: Context,
            isCompactAgeMode: Boolean
        ): EventItemData {
            val eventDate = event.date
            return EventItemData(
                id = event.id,
                event = event,
                formattedDate = eventDate.toFormattedDate(context, DateShowingMode.VIEW_MODE),
                showTurnsWord = eventDate.isUpcomingEvent(),
                formattedAge = eventDate.getAgeText(
                    context = context,
                    isCompactAgeMode = isCompactAgeMode
                ),
                daysToEventText = eventDate.getDaysToThisEvent(context),
                isUpcoming = eventDate.isUpcomingEvent(),
                zodiac = eventDate.getZodiacSign()
            )
        }
    }
}

private fun EventDate.getAgeText(
    context: Context,
    today: Calendar = getTodayCalendar(),
    isCompactAgeMode: Boolean
): String? {
    if (year <= 0) return null
    val currentYear = today.toLocalDate().year
    val age = getAge(currentYear)
    if (age <= 0) return null

    return if (isCompactAgeMode) {
        age.toString()
    } else {
        context.resources.getQuantityString(R.plurals.event_age_short_label, age, age)
    }
}

private fun EventDate.getDaysToThisEvent(
    context: Context,
    today: Calendar = getTodayCalendar()
): String {
    return if (isPastEvent(today)) {
        getEventDaysAgo(context, today)
    } else {
        getEventDaysLeft(context, today)
    }
}

private fun EventDate.getEventDaysAgo(
    context: Context,
    todayCalendar: Calendar = getTodayCalendar()
): String {
    val todayLocalDate = todayCalendar.toLocalDate()
    val eventLocalDate = getCalendarWithCurrentYear(todayLocalDate.year).toLocalDate()
    val daysCount = Days.daysBetween(eventLocalDate, todayLocalDate).days
    return when (daysCount) {
        0 -> context.resources.getString(R.string.today)

        1 -> context.resources.getString(R.string.yesterday)

        else -> context.resources.getQuantityString(
            R.plurals.days_ago_pattern,
            daysCount,
            daysCount
        )
    }
}

private fun EventDate.getEventDaysLeft(
    context: Context,
    todayCalendar: Calendar = getTodayCalendar()
): String {
    val todayLocalDate = todayCalendar.toLocalDate()
    val eventLocalDate = getCalendarWithCurrentYear(todayLocalDate.year).toLocalDate()
    val daysCount = Days.daysBetween(todayLocalDate, eventLocalDate).days
    return when (daysCount) {
        0 -> context.resources.getString(R.string.today)

        1 -> context.resources.getString(R.string.tomorrow)

        else -> context.resources.getQuantityString(
            R.plurals.days_left_short_pattern,
            daysCount,
            daysCount
        )
    }
}

private fun EventDate.getZodiacSign(): ZodiacSign {
    when (month) {
        // January
        1 -> return if (day <= 19) ZodiacSign.CAPRICORN else ZodiacSign.AQUARIUS
        // February
        2 -> return if (day <= 18) ZodiacSign.AQUARIUS else ZodiacSign.PISCES
        // March
        3 -> return if (day <= 20) ZodiacSign.PISCES else ZodiacSign.ARIES
        // April
        4 -> return if (day <= 19) ZodiacSign.ARIES else ZodiacSign.TAURUS
        // May
        5 -> return if (day <= 20) ZodiacSign.TAURUS else ZodiacSign.GEMINI
        // June
        6 -> return if (day <= 20) ZodiacSign.GEMINI else ZodiacSign.CANCER
        // July
        7 -> return if (day <= 22) ZodiacSign.CANCER else ZodiacSign.LEO
        // August
        8 -> return if (day <= 22) ZodiacSign.LEO else ZodiacSign.VIRGO
        // September
        9 -> return if (day <= 22) ZodiacSign.VIRGO else ZodiacSign.LIBRA
        // October
        10 -> return if (day <= 22) ZodiacSign.LIBRA else ZodiacSign.SCORPIO
        // November
        11 -> return if (day <= 21) ZodiacSign.SCORPIO else ZodiacSign.SAGITTARIUS
        // December
        12 -> return if (day <= 21) ZodiacSign.SAGITTARIUS else ZodiacSign.CAPRICORN

        else -> return ZodiacSign.UNKNOWN
    }
}