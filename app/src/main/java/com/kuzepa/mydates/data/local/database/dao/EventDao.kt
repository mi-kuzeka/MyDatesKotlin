package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventWithTypeAndLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Upsert()
    suspend fun upsertEvent(event: EventEntity)

    @Transaction
    @Query("SELECT * FROM events WHERE id=:id;")
    suspend fun getEventById(id: Int): EventWithTypeAndLabels?

    @Query("DELETE FROM events WHERE id=:id;")
    suspend fun deleteEventById(id: Int)

    @Transaction
    @Query("SELECT * FROM events;")
    fun getAllEvents(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY name ASC;")
    fun getAllEventsSortByNameAsc(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY name DESC;")
    fun getAllEventsSortByNameDesc(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY notification_date ASC;")
    fun getAllEventsSortByDateAsc(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY notification_date DESC;")
    fun getAllEventsSortByDateDesc(): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month;")
    fun getEventsByMonth(month: Int): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month ORDER BY name ASC;")
    fun getEventsByMonthSortByNameAsc(month: Int): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month ORDER BY name DESC;")
    fun getEventsByMonthSortByNameDesc(month: Int): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month ORDER BY day ASC;")
    fun getEventsByMonthSortByDateAsc(month: Int): Flow<List<EventWithTypeAndLabels>>

    @Transaction
    @Query("SELECT * FROM events WHERE month = :month ORDER BY day DESC;")
    fun getEventsByMonthSortByDateDesc(month: Int): Flow<List<EventWithTypeAndLabels>>
}