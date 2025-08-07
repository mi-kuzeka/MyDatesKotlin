package com.kuzepa.mydates.ui.activities.more.eventtype

import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.NotificationFilterState

data class EventTypeUiState(
    val eventType: EventType? = null,
    val name: String = "",
    val nameValidationError: String? = null,
    val isDefault: Boolean = false,
    val showZodiac: Boolean = false,
    val notificationState: NotificationFilterState = NotificationFilterState.FILTER_STATE_ON
)