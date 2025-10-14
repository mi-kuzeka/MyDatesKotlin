package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventTypeDao {
    @Upsert()
    suspend fun upsertEventType(eventTypeEntity: EventTypeEntity)

    @Transaction
    suspend fun clearDefaultAndUpsertEventType(eventTypeEntity: EventTypeEntity) {
        clearDefaultEventType()
        upsertEventType(eventTypeEntity)
    }

    @Query("SELECT * FROM event_types WHERE id = :id;")
    suspend fun getEventTypeById(id: String): EventTypeEntity?

    @Query("DELETE FROM event_types WHERE id = :id;")
    suspend fun deleteEventTypeById(id: String)

    @Query("SELECT * FROM event_types WHERE is_default = 1;")
    suspend fun getDefaultEventType(): EventTypeEntity?

    @Query("UPDATE event_types SET is_default=0 WHERE is_default = 1;")
    suspend fun clearDefaultEventType()

    @Query("SELECT * FROM event_types ORDER BY name ASC;")
    fun getAllEventTypes(): Flow<List<EventTypeEntity>>
}