package com.kuzepa.mydates.domain.model.notification

enum class AdditionalNotification(val daysBefore: Int) {
    ONE_DAY_BEFORE(daysBefore = 1),
    TWO_DAYS_BEFORE(daysBefore = 2),
    THREE_DAYS_BEFORE(daysBefore = 3),
    FOUR_DAYS_BEFORE(daysBefore = 4),
    FIVE_DAYS_BEFORE(daysBefore = 5),
    ONE_WEEK_BEFORE(daysBefore = 7),
    TWO_WEEKS_BEFORE(daysBefore = 14),
    ONE_MONTH_BEFORE(daysBefore = 30),
}