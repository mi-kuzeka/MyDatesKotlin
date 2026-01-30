package com.kuzepa.mydates.feature.more.eventtype

sealed class EventTypeScreenEvent {
    data class NameChanged(val name: String) : EventTypeScreenEvent()
    data class IsDefaultChanged(val isDefault: Boolean) : EventTypeScreenEvent()
    data class ShowZodiacChanged(val showZodiac: Boolean) : EventTypeScreenEvent()
    data class ShowNotificationsChanged(val showNotifications: Boolean) : EventTypeScreenEvent()
    data object Delete : EventTypeScreenEvent()
    data object Save : EventTypeScreenEvent()
}