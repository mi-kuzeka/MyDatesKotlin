package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun addEvent(event: Event)
    suspend fun getAllEvents(): Flow<List<Event>>
    suspend fun getEventsByMonth(month: Int): Flow<List<Event>>
}