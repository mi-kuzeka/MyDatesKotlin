package com.kuzepa.mydates.ui.activities.more.eventtype

sealed class EventTypeScreenEvent {
    data class NameChanged(val name: String) : EventTypeScreenEvent()
    data class IsDefaultChanged(val isDefault: Boolean) : EventTypeScreenEvent()
    data class ShowZodiacChanged(val showZodiac: Boolean) : EventTypeScreenEvent()
    data class ShowNotificationsChanged(val showNotifications: Boolean) : EventTypeScreenEvent()
    object Delete : EventTypeScreenEvent()
    object Save : EventTypeScreenEvent()
}