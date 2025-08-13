package com.kuzepa.mydates.data.local.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Provider

class DataBaseInitializer(
    private val eventTypeProvider: Provider<EventTypeDao>,
    private val birthdayEventTypeName: String
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDataBase()
        }
    }

    private suspend fun populateDataBase() {
        populateEventTypes()
    }

    private suspend fun populateEventTypes() {
        val defaultEventType = EventTypeEntity(
            id = UUID.randomUUID().toString(),
            name = birthdayEventTypeName,
            isDefault = true,
            notificationState = 0, // Filter state = on
            showZodiac = true
        )
        eventTypeProvider.get().upsertEventType(defaultEventType)
    }
}