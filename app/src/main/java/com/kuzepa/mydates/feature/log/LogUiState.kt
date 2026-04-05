package com.kuzepa.mydates.feature.log

data class LogUiState(
    val loadingState: LogLoadingState = LogLoadingState.Loading,
    val sharingEmailIntentState: LogSharingEmailIntentState = LogSharingEmailIntentState.Loading,
    val logMessage: String = "",
    val errorMessage: String = ""
)