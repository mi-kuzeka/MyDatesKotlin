package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventWithTypeAndLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = REPLACE)
    fun addEvent(event: EventEntity)

    @Transaction
    @Query("SELECT * FROM events;")
    fun getAllEvents(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month;")
    fun getEventsByMonth(month: Int): Flow<List<EventWithTypeAndLabels>>
}