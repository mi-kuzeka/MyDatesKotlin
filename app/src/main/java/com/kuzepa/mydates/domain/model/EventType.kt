package com.kuzepa.mydates.domain.model

data class EventType(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val notificationState: NotificationFilterState,
    val showZodiac: Boolean
)
