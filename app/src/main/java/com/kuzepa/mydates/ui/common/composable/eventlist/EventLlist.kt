package com.kuzepa.mydates.ui.common.composable.eventlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.ui.common.composable.state.EmptyView

@Composable
fun EventList(
    events: List<Event>,
    onNavigateToEventEditor: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        EmptyView(modifier)
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(rememberNestedScrollInteropConnection())
        ) {
            items(
                items = events,
                key = { event -> event.id }
            ) { event ->
                EventItem(
                    event = event,
                    onNavigateToEventEditor = onNavigateToEventEditor
                )
            }
        }
    }
}