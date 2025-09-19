package com.kuzepa.mydates.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = EventTypeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("event_type_id"),
            onDelete = RESTRICT
        )
    ]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "month")
    val month: Int,
    @ColumnInfo(name = "day")
    val day: Int,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "event_type_id")
    val eventTypeId: String,
    @ColumnInfo(name = "image")
    val image: ByteArray?,
    @ColumnInfo(name = "notification_date")
    val notificationDate: Int
)
