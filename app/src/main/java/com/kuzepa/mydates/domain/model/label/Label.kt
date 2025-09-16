package com.kuzepa.mydates.domain.model.label

import com.kuzepa.mydates.domain.model.NotificationFilterState

data class Label(
    val id: String,
    val name: String,
    val color: Int,
    val notificationState: NotificationFilterState,
    val iconId: Int
)
