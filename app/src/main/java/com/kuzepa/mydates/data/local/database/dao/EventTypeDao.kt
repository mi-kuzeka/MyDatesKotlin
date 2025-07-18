package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity

@Dao
interface EventTypeDao {
    @Insert(onConflict = REPLACE)
    suspend fun addEventType(eventTypeEntity: EventTypeEntity)
}