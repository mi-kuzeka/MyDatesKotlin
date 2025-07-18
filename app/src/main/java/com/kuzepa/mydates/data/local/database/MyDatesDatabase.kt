package com.kuzepa.mydates.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuzepa.mydates.data.local.database.dao.EventDao
import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import com.kuzepa.mydates.data.local.database.dao.LabelDao
import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventLabelCrossRef
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import com.kuzepa.mydates.data.local.database.entity.LabelEntity

@Database(
    entities = [
        EventEntity::class,
        EventTypeEntity::class,
        LabelEntity::class,
        EventLabelCrossRef::class
    ],
    version = 14
)
abstract class MyDatesDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun labelDao(): LabelDao

    companion object {
        private const val DATABASE_NAME = "mydates.db"

        fun buildDatabase(context: Context): MyDatesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MyDatesDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}