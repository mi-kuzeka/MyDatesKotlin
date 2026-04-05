package com.kuzepa.mydates.feature.log


sealed class ReadingLogResult {
    data object NoFile : ReadingLogResult()
    data object EmptyFile : ReadingLogResult()
    data object Success : ReadingLogResult()
}