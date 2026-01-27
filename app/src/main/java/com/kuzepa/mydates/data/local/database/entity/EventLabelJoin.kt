package com.kuzepa.mydates.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "event_label_join",
    primaryKeys = ["event_id", "label_id"],
    indices = [
        Index("event_id"),
        Index("label_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = LabelEntity::class,
            parentColumns = ["id"],
            childColumns = ["label_id"],
            onDelete = CASCADE
        ),
    ]
)
data class EventLabelJoin(
    @ColumnInfo(name = "event_id") val eventId: Long,
    @ColumnInfo(name = "label_id") val labelId: String
)
