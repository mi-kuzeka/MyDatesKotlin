package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import com.kuzepa.mydates.domain.mapper.toEventType
import com.kuzepa.mydates.domain.mapper.toEventTypeEntity
import com.kuzepa.mydates.domain.model.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DbEventTypeRepository @Inject constructor(
    private val eventTypeDao: EventTypeDao
) : EventTypeRepository {
    override suspend fun getEventTypeById(id: String): EventType? {
        return eventTypeDao.getEventTypeById(id)?.toEventType()
    }

    override suspend fun addEventType(eventType: EventType) {
        eventTypeDao.addEventType(eventType.toEventTypeEntity())
    }

    override suspend fun getDefaultEventType(): EventType? {
        return eventTypeDao.getDefaultEventType()?.toEventType()
    }

    override fun getAllEventTypes(): Flow<List<EventType>> {
        return eventTypeDao.getAllEventTypes().map { eventTypes ->
            eventTypes.map { eventTypeEntity -> eventTypeEntity.toEventType()  }
        }
    }
}