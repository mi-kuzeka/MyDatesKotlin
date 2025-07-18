package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.EventDao
import com.kuzepa.mydates.domain.mapper.toEvent
import com.kuzepa.mydates.domain.mapper.toEventEntity
import com.kuzepa.mydates.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DbEventRepository @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    override suspend fun addEvent(event: Event) {
        eventDao.addEvent(event.toEventEntity())
    }

    override suspend fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { events -> events.map { it.toEvent() } }
    }

    override suspend fun getEventsByMonth(month: Int): Flow<List<Event>> {
        return eventDao.getEventsByMonth(month).map { events -> events.map { it.toEvent() } }
    }
}