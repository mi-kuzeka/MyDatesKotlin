package com.kuzepa.mydates.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labels")
data class LabelEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name", defaultValue = "")
    val name: String,
    @ColumnInfo(name = "color", defaultValue = "1")
    val color: Int,
    @ColumnInfo(name = "notification_state", defaultValue = "0")
    val notificationState: Int,
    @ColumnInfo(name = "icon_id", defaultValue = "0")
    val iconId: Int,
    @ColumnInfo(name = "emoji", defaultValue = "")
    val emoji: String
)
