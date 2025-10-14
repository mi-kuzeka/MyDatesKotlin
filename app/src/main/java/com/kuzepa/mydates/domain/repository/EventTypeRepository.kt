package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.EventType
import kotlinx.coroutines.flow.Flow

interface EventTypeRepository {
    suspend fun upsertEventType(eventType: EventType)
    suspend fun clearDefaultAndUpsertEventType(eventType: EventType)
    suspend fun getEventTypeById(id: String): EventType?
    suspend fun deleteEventTypeById(id: String)
    suspend fun getDefaultEventType(): EventType?
    fun getAllEventTypes(): Flow<List<EventType>>
}