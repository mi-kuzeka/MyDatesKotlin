package com.kuzepa.mydates.domain.mapper

import com.kuzepa.mydates.data.local.database.entity.EventLabelJoin
import com.kuzepa.mydates.domain.model.EventLabelLink

fun EventLabelLink.toEventLabelJoin() = EventLabelJoin(
    eventId = eventId,
    labelId = labelId
)

fun EventLabelJoin.toEventLabelLink() = EventLabelLink(
    eventId = eventId,
    labelId = labelId
)

fun List<String>.toEventLabelJoin(eventId: Long): List<EventLabelJoin> {
    return this.map { EventLabelJoin(eventId, it) }
}