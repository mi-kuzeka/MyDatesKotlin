package com.kuzepa.mydates.domain.model

import com.kuzepa.mydates.domain.model.notification.NotificationFilterState

data class EventType(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val notificationState: NotificationFilterState,
    val showZodiac: Boolean
)
