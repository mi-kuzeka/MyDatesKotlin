package com.kuzepa.mydates.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LabelWithEvents(
    @Embedded val label: LabelEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            EventLabelCrossRef::class,
            parentColumn = "label_id",
            entityColumn = "event_id"
        )
    )
    val events: List<EventEntity>
)
