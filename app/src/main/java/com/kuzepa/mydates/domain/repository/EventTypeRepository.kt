package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.EventType
import kotlinx.coroutines.flow.Flow

interface EventTypeRepository {
    suspend fun getEventTypeById(id: String): EventType?
    suspend fun addEventType(eventType: EventType)
    suspend fun getDefaultEventType(): EventType?
    fun getAllEventTypes(): Flow<List<EventType>>
}