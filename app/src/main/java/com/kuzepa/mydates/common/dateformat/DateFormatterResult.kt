package com.kuzepa.mydates.common.dateformat

import androidx.compose.runtime.Stable
import com.kuzepa.mydates.domain.model.EventDate

@Stable
sealed interface DateFormatterResult {
    data class OK(val eventDate: EventDate) : DateFormatterResult
    data class ShortLength(val limit: Int) : DateFormatterResult
    data class Error(val e: Throwable) : DateFormatterResult
}