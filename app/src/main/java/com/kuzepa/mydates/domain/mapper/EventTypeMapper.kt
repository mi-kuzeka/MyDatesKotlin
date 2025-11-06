package com.kuzepa.mydates.domain.mapper

import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState

fun EventTypeEntity.toEventType(): EventType {
    return EventType(
        id = id,
        name = name,
        isDefault = isDefault,
        notificationState = NotificationFilterState.fromInt(notificationState),
        showZodiac = showZodiac
    )
}

fun EventType.toEventTypeEntity(): EventTypeEntity {
    return EventTypeEntity(
        id = id,
        name = name,
        isDefault = isDefault,
        notificationState = notificationState.value,
        showZodiac = showZodiac
    )
}