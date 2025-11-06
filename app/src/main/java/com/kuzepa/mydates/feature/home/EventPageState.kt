package com.kuzepa.mydates.feature.home

import androidx.compose.runtime.Stable
import com.kuzepa.mydates.feature.eventlist.EventListItem

@Stable
sealed interface EventPageState {
    object Loading : EventPageState
    data class Success(val eventListItems: List<EventListItem>) : EventPageState
    data class Error(val message: String) : EventPageState
}