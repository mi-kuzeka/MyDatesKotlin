package com.kuzepa.mydates.feature.home

import androidx.compose.runtime.Stable
import com.kuzepa.mydates.feature.eventlist.EventListGrouping

@Stable
sealed interface EventPageState {
    object Loading : EventPageState
    data class Success(val eventListGrouping: EventListGrouping) : EventPageState
    data class Error(val message: String) : EventPageState
}