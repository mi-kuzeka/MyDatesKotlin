package com.kuzepa.mydates.feature.logs

import android.content.Intent
import androidx.compose.runtime.Stable

@Stable
sealed interface LogsSharingEmailIntentState {
    data object Loading : LogsSharingEmailIntentState
    data class Success(val emailClientIntent: Intent) : LogsSharingEmailIntentState
    data class Error(val message: String) : LogsSharingEmailIntentState
}