package com.kuzepa.mydates.feature.eventtypelist

import androidx.compose.runtime.Stable
import com.kuzepa.mydates.domain.model.EventType

@Stable
sealed interface EventTypeListUiState {
    data object Loading : EventTypeListUiState
    data class Success(val eventTypeList: List<EventType>) : EventTypeListUiState
    data class Error(val message: String) : EventTypeListUiState
}