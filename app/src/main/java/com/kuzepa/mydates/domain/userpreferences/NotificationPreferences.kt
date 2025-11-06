package com.kuzepa.mydates.domain.userpreferences

import com.kuzepa.mydates.domain.model.notification.AdditionalNotification
import com.kuzepa.mydates.domain.model.notification.NotificationTime

data class NotificationOnDayPreferences(
    val notificationTime: NotificationTime
)

data class AdditionalNotificationsPreferences(
    val notificationTimeIsSameAsMain: Boolean,
    val notificationTime: NotificationTime,
    val daysBefore: List<AdditionalNotification>
)