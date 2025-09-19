package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.EventLabelLink

interface EventLabelJoinRepository {
    suspend fun upsertLink(eventLabelLink: EventLabelLink)
    suspend fun upsertLinks(eventLabelLinks: List<EventLabelLink>)
    suspend fun deleteLink(eventLabelLink: EventLabelLink)
    suspend fun deleteLinks(eventLabelLinks: List<EventLabelLink>)
    suspend fun getLinksForEvent(eventId: Long): List<EventLabelLink>
    suspend fun getLinksForLabel(labelId: String): List<EventLabelLink>
}