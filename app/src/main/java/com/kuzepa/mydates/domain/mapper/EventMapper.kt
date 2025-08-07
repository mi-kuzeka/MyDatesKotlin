package com.kuzepa.mydates.domain.mapper

import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventWithTypeAndLabels
import com.kuzepa.mydates.domain.converter.getNotificationDateCode
import com.kuzepa.mydates.domain.converter.toBitmap
import com.kuzepa.mydates.domain.converter.toByteArray
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate

fun EventWithTypeAndLabels.toEvent(): Event {
    return Event(
        id = event.id,
        name = event.name,
        date = EventDate(month = event.month, day = event.day, year = event.year),
        type = eventType.toEventType(),
        notes = event.notes,
        image = event.image.toBitmap(),
        notificationDateCode = event.notificationDate,
        labels = labels.map { it.toLabel() }
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        month = date.month,
        day = date.day,
        year = date.year,
        notes = notes,
        eventTypeId = type.id,
        image = image.toByteArray(),
        notificationDate = date.getNotificationDateCode()
    )
}