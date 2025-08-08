package com.kuzepa.mydates.ui.activities.home

import com.kuzepa.mydates.domain.model.Event

sealed interface EventPageState {
    object Loading : EventPageState
    data class Success(val events: List<Event>) : EventPageState
    data class Error(val message: String) : EventPageState
}