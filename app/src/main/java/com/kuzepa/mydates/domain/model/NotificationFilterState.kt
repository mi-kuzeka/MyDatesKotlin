package com.kuzepa.mydates.domain.model

enum class NotificationFilterState(val value: Int) {
    FILTER_STATE_ON(0),
    FILTER_STATE_OFF(1),
    FILTER_STATE_FORBIDDEN(2);

    companion object {
        fun fromInt(value: Int): NotificationFilterState {
            return try {
                entries.first { it.value == value }
            } catch (_: NoSuchElementException) {
                FILTER_STATE_ON
            }
        }
    }
}