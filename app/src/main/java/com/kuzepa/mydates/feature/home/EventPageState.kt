package com.kuzepa.mydates.feature.home

import androidx.compose.runtime.Stable
import com.kuzepa.mydates.domain.model.Event

@Stable
sealed interface EventPageState {
    object Loading : EventPageState
    data class Success(val events: List<Event>) : EventPageState
    data class Error(val message: String) : EventPageState
}