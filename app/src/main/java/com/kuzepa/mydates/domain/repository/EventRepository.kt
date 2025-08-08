package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.SortOption
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun upsertEvent(event: Event)
    suspend fun getEventById(id: Int): Event?
    suspend fun deleteEventById(id: Int)
    fun getAllEvents(sortOption: SortOption = SortOption.UNSPECIFIED): Flow<List<Event>>
    fun getEventsByMonth(month: Int, sortOption: SortOption): Flow<List<Event>>
}