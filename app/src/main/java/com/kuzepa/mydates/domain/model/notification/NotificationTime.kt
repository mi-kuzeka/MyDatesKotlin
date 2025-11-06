package com.kuzepa.mydates.domain.model.notification

import com.kuzepa.mydates.domain.formatter.toFixedLengthString

data class NotificationTime(
    val hours: Int,
    val minutes: Int
) {
    fun getEndTime(): NotificationTime = plusMinutes(30)

    override fun toString(): String {
        return "$hours:${minutes.toFixedLengthString(2)}"
    }
}

fun NotificationTime.plusHours(hoursToAdd: Int): NotificationTime {
    return NotificationTime(hours.plusHours(hoursToAdd), minutes)
}

private fun Int.plusHours(hoursToAdd: Int): Int {
    val sum = this + hoursToAdd
    return if (sum > 23) sum % 24 else sum
}

fun NotificationTime.plusMinutes(minutesToAdd: Int): NotificationTime {
    val sum = minutes + minutesToAdd
    val hoursToAdd = sum / 60
    val resultMinutes = sum % 60
    return NotificationTime(hours.plusHours(hoursToAdd), resultMinutes)
}