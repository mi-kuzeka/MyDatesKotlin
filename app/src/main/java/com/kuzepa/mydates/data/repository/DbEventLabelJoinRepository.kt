package com.kuzepa.mydates.data.repository

import com.kuzepa.mydates.data.local.database.dao.EventLabelJoinDao
import com.kuzepa.mydates.domain.mapper.toEventLabelJoin
import com.kuzepa.mydates.domain.mapper.toEventLabelLink
import com.kuzepa.mydates.domain.model.EventLabelLink
import com.kuzepa.mydates.domain.repository.EventLabelJoinRepository
import javax.inject.Inject

class DbEventLabelJoinRepository @Inject constructor(
    private val eventLabelJoinDao: EventLabelJoinDao
) : EventLabelJoinRepository {
    override suspend fun upsertLink(eventLabelLink: EventLabelLink) {
        eventLabelJoinDao.upsert(eventLabelLink.toEventLabelJoin())
    }

    override suspend fun upsertLinks(eventLabelLinks: List<EventLabelLink>) {
        eventLabelJoinDao.upsertLinks(
            eventLabelLinks.map { it.toEventLabelJoin() }
        )
    }

    override suspend fun deleteLink(eventLabelLink: EventLabelLink) {
        eventLabelJoinDao.deleteLink(eventLabelLink.toEventLabelJoin())
    }

    override suspend fun deleteLinks(eventLabelLinks: List<EventLabelLink>) {
        eventLabelJoinDao.deleteLinks(eventLabelLinks.map { it.toEventLabelJoin() })
    }

    override suspend fun getLinksForEvent(eventId: Long): List<EventLabelLink> {
        return eventLabelJoinDao.getLinksForEvent(eventId)
            .map { it.toEventLabelLink() }
    }

    override suspend fun getLinksForLabel(labelId: String): List<EventLabelLink> {
        return eventLabelJoinDao.getLinksForLabel(labelId)
            .map { it.toEventLabelLink() }
    }
}