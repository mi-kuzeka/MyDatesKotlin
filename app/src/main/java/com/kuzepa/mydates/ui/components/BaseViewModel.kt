package com.kuzepa.mydates.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<SCREEN_EVENT> : ViewModel() {
    abstract fun onEvent(event: SCREEN_EVENT)
}

@Composable
fun <SCREEN_EVENT> BaseViewModel<SCREEN_EVENT>.rememberOnEvent(): (SCREEN_EVENT) -> Unit {
    return remember(this) { { event -> this.onEvent(event) } }
}