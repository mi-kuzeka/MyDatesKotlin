package com.kuzepa.mydates.feature.logs


sealed class ReadingLogsResult {
    data object NoFile : ReadingLogsResult()
    data object EmptyFile : ReadingLogsResult()
    data object Success : ReadingLogsResult()
}