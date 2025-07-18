package com.kuzepa.mydates.domain.mapper

import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventWithTypeAndLabels
import com.kuzepa.mydates.domain.converter.toBitmap
import com.kuzepa.mydates.domain.converter.toByteArray
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate

fun EventWithTypeAndLabels.toEvent(): Event {
    return Event(
        id = event.id,
        name = event.name,
        eventDate = EventDate(month = event.month, day = event.day, year = event.year),
        eventType = eventType.toEventType(),
        notes = event.notes,
        image = event.image.toBitmap(),
        notificationDate = event.notificationDate,
        labels = labels.map { it.toLabel() }
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        month = eventDate.month,
        day = eventDate.day,
        year = eventDate.year,
        notes = notes,
        eventTypeId = eventType.id,
        image = image.toByteArray(),
        notificationDate = notificationDate
    )
}