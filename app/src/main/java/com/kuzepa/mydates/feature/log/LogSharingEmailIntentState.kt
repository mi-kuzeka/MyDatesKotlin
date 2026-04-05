package com.kuzepa.mydates.feature.log

import android.content.Intent
import androidx.compose.runtime.Stable

@Stable
sealed interface LogSharingEmailIntentState {
    data object Loading : LogSharingEmailIntentState
    data class Success(val emailClientIntent: Intent) : LogSharingEmailIntentState
    data class Error(val message: String) : LogSharingEmailIntentState
}