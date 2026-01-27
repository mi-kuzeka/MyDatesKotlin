package com.kuzepa.mydates.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_types")
data class EventTypeEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name", defaultValue = "")
    val name: String,
    @ColumnInfo(name = "is_default", defaultValue = "0")
    val isDefault: Boolean,
    @ColumnInfo(name = "notification_state", defaultValue = "0")
    val notificationState: Int,
    @ColumnInfo(name = "show_zodiac", defaultValue = "0")
    val showZodiac: Boolean
)

