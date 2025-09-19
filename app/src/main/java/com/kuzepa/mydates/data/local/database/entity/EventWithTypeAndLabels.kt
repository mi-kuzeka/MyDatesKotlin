package com.kuzepa.mydates.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EventWithTypeAndLabels(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "event_type_id",
        entityColumn = "id",
        associateBy = Junction(
            EventTypeEntity::class,
            parentColumn = "id"
        )
    )
    val eventType: EventTypeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            EventLabelJoin::class,
            parentColumn = "event_id",
            entityColumn = "label_id"
        )
    )
    val labels: List<LabelEntity>
)