package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.EventType

interface EventTypeRepository {
    suspend fun addEventType(eventType: EventType)
    suspend fun getDefaultEventType(): EventType?
}