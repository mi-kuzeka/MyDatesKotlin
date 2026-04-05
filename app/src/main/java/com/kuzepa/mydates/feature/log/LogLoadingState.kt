package com.kuzepa.mydates.feature.log

import androidx.compose.runtime.Stable

@Stable
sealed interface LogLoadingState {
    data object Loading : LogLoadingState
    data object Success : LogLoadingState
    data class NoLog(val message: String) : LogLoadingState
    data class Error(val message: String) : LogLoadingState
}