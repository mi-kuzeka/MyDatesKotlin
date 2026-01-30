package com.kuzepa.mydates.feature.logs

import androidx.compose.runtime.Stable

@Stable
sealed interface LogsLoadingState {
    data object Loading : LogsLoadingState
    data object Success : LogsLoadingState
    data class NoLogs(val message: String) : LogsLoadingState
    data class Error(val message: String) : LogsLoadingState
}