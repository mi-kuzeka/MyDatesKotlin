package com.kuzepa.mydates.feature.more.label

import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.label.LabelIcon

sealed class LabelScreenEvent {
    data class NameChanged(val name: String) : LabelScreenEvent()
    data class ColorChanged(val color: Int) : LabelScreenEvent()
    data class IconChanged(val icon: LabelIcon) : LabelScreenEvent()
    data class NotificationStateChanged(val notificationState: NotificationFilterState) :
        LabelScreenEvent()

    object Delete : LabelScreenEvent()
    object Save : LabelScreenEvent()
}