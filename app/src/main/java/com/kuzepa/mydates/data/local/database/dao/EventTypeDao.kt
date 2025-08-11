package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventTypeDao {
    @Upsert()
    suspend fun upsertEventType(eventTypeEntity: EventTypeEntity)

    @Query("SELECT * FROM event_types WHERE id = :id;")
    suspend fun getEventTypeById(id: String): EventTypeEntity?

    @Query("DELETE FROM event_types WHERE id = :id;")
    suspend fun deleteEventTypeById(id: String)

    @Query("SELECT * FROM event_types WHERE is_default = 1;")
    suspend fun getDefaultEventType(): EventTypeEntity?

    @Query("SELECT * FROM event_types ORDER BY name ASC;")
    fun getAllEventTypes(): Flow<List<EventTypeEntity>>
}