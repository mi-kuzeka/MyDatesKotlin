package com.kuzepa.mydates.feature.logs

data class LogsUiState(
    val loadingState: LogsLoadingState = LogsLoadingState.Loading,
    val sharingEmailIntentState: LogsSharingEmailIntentState = LogsSharingEmailIntentState.Loading,
    val logsMessage: String = "",
    val errorMessage: String = ""
)