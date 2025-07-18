package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import com.kuzepa.mydates.domain.mapper.toEventTypeEntity
import com.kuzepa.mydates.domain.model.EventType
import javax.inject.Inject

class DbEventTypeRepository @Inject constructor(
    private val eventTypeDao: EventTypeDao
) : EventTypeRepository {
    override suspend fun addEventType(eventType: EventType) {
        eventTypeDao.addEventType(eventType.toEventTypeEntity())
    }

}