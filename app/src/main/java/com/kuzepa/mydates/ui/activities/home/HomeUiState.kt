package com.kuzepa.mydates.ui.activities.home

import com.kuzepa.mydates.domain.model.Event

sealed interface HomeUiState {
    data class Success(val events: List<Event>) : HomeUiState
    data object Loading : HomeUiState
    data object Error : HomeUiState
}