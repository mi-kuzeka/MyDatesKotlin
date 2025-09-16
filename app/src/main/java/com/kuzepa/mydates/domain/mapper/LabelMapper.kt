package com.kuzepa.mydates.domain.mapper

import com.kuzepa.mydates.data.local.database.entity.LabelEntity
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.label.Label

fun LabelEntity.toLabel(): Label {
    return Label(
        id = id,
        name = name,
        color = color,
        notificationState = NotificationFilterState.fromInt(notificationState),
        iconId = iconId
    )
}

fun Label.toLabelEntity(): LabelEntity {
    return LabelEntity(
        id = id,
        name = name,
        color = color,
        notificationState = notificationState.value,
        iconId = iconId
    )
}