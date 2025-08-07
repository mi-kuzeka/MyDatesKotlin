package com.kuzepa.mydates.ui.activities.more.eventtype.composable

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuzepa.mydates.ui.activities.more.eventtype.EventTypeViewModel

@Composable
fun EventTypeScreen(
    viewModel: EventTypeViewModel = hiltViewModel(),
    id: Int?,
    onNavigateBack: () -> Unit
) {

}