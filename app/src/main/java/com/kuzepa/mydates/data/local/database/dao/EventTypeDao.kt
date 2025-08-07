package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity

@Dao
interface EventTypeDao {
    @Query("SELECT * FROM event_types WHERE id = :id;")
    suspend fun getEventTypeById(id: String): EventTypeEntity?

    @Insert(onConflict = REPLACE)
    suspend fun addEventType(eventTypeEntity: EventTypeEntity)

    @Query("SELECT * FROM event_types WHERE is_default = 1;")
    suspend fun getDefaultEventType(): EventTypeEntity?
}