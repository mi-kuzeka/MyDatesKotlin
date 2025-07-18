package com.kuzepa.mydates.ui.activities.main

import com.kuzepa.mydates.domain.model.Event

sealed interface MainUiState {
    data class Success(val events: List<Event>) : MainUiState
    data object Loading : MainUiState
    data object Error : MainUiState
}