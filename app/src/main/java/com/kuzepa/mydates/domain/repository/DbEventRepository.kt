package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.EventDao
import com.kuzepa.mydates.domain.mapper.toEvent
import com.kuzepa.mydates.domain.mapper.toEventEntity
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DbEventRepository @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    override suspend fun addEvent(event: Event) {
        eventDao.addEvent(event.toEventEntity())
    }

    override suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)?.toEvent()
    }

    override fun getAllEvents(sortOption: SortOption): Flow<List<Event>> {
        return when (sortOption) {
            SortOption.BY_NAME_ASC -> eventDao.getAllEventsSortByNameAsc()
            SortOption.BY_NAME_DESC -> eventDao.getAllEventsSortByNameDesc()
            SortOption.BY_DATE_ASC -> eventDao.getAllEventsSortByDateAsc()
            SortOption.BY_DATE_DESC -> eventDao.getAllEventsSortByDateDesc()
            else -> eventDao.getAllEvents()
        }.map { events -> events.map { it.toEvent() } }
    }

    override fun getEventsByMonth(
        month: Int,
        sortOption: SortOption
    ): Flow<List<Event>> {
        return when (sortOption) {
            SortOption.BY_NAME_ASC -> eventDao.getEventsByMonthSortByNameAsc(month)
            SortOption.BY_NAME_DESC -> eventDao.getEventsByMonthSortByNameDesc(month)
            SortOption.BY_DATE_ASC -> eventDao.getEventsByMonthSortByDateAsc(month)
            SortOption.BY_DATE_DESC -> eventDao.getEventsByMonthSortByDateDesc(month)
            else -> eventDao.getEventsByMonth(month)
        }.map { events -> events.map { it.toEvent() } }
    }
}