package com.kuzepa.mydates.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuzepa.mydates.R
import com.kuzepa.mydates.data.local.database.dao.EventDao
import com.kuzepa.mydates.data.local.database.dao.EventLabelJoinDao
import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import com.kuzepa.mydates.data.local.database.dao.LabelDao
import com.kuzepa.mydates.data.local.database.entity.EventEntity
import com.kuzepa.mydates.data.local.database.entity.EventLabelJoin
import com.kuzepa.mydates.data.local.database.entity.EventTypeEntity
import com.kuzepa.mydates.data.local.database.entity.LabelEntity
import com.kuzepa.mydates.data.local.database.migrations.Migrations
import javax.inject.Provider

@Database(
    entities = [
        EventEntity::class,
        EventTypeEntity::class,
        LabelEntity::class,
        EventLabelJoin::class
    ],
    // should be equal to the app version in which the db is updated
    version = 32
)

abstract class MyDatesDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun labelDao(): LabelDao
    abstract fun eventLabelJoinDao(): EventLabelJoinDao

    companion object {
        private const val DATABASE_NAME = "mydates.db"

        fun buildDatabase(
            context: Context,
            eventTypeProvider: Provider<EventTypeDao>
        ): MyDatesDatabase {
            val birthdayEventTypeName = context.applicationContext
                .getString(R.string.event_type_birthday_name)
            return Room.databaseBuilder(
                context.applicationContext,
                MyDatesDatabase::class.java,
                DATABASE_NAME
            ).addCallback(
                DataBaseInitializer(
                    eventTypeProvider = eventTypeProvider,
                    birthdayEventTypeName = birthdayEventTypeName
                )
            ).addMigrations(
                Migrations.getMigration1to2(),
                Migrations.getMigration2to3(),
                Migrations.getMigration3to4(),
                Migrations.getMigration4to13(birthdayEventTypeName),
                Migrations.getMigration13to32()
            ).build()
        }
    }
}

class MyDatesDatabaseInstance {
    companion object {
        @Volatile
        private var INSTANCE: MyDatesDatabase? = null

        fun getInstance(
            context: Context,
            eventTypeProvider: Provider<EventTypeDao>
        ): MyDatesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = MyDatesDatabase
                    .buildDatabase(
                        context,
                        eventTypeProvider
                    )
                INSTANCE = instance
                instance
            }
        }
    }
}