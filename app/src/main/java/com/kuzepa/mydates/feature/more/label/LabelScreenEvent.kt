package com.kuzepa.mydates.feature.more.label

import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState

sealed class LabelScreenEvent {
    data class NameChanged(val name: String) : LabelScreenEvent()
    data class ColorChanged(val color: Int?) : LabelScreenEvent()
    data class IconChanged(val icon: LabelIcon) : LabelScreenEvent()
    data class EmojiPicked(val emoji: String?) : LabelScreenEvent()
    data class NotificationStateChanged(val notificationState: NotificationFilterState) :
        LabelScreenEvent()
    object ExpandIcons : LabelScreenEvent()
    object CollapseIcons : LabelScreenEvent()
    object ShowEmojiPicker : LabelScreenEvent()
    object HideEmojiPicker : LabelScreenEvent()
    object Delete : LabelScreenEvent()
    object Save : LabelScreenEvent()
}