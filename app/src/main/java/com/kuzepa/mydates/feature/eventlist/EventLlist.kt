package com.kuzepa.mydates.feature.eventlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.kuzepa.mydates.ui.components.stateview.EmptyView

@Composable
fun EventList(
    eventListItems: List<EventListItem>,
    onNavigateToEventEditor: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (eventListItems.isEmpty()) {
        EmptyView(modifier)
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(rememberNestedScrollInteropConnection())
        ) {
            items(
                items = eventListItems,
                key = { eventListItem -> eventListItem.id },
                contentType = { eventListItem ->
                    when (eventListItem) {
                        is EventItemData -> "EventItemData"
                        is EventListDelimiter -> "EventListDelimiter"
                    }
                }
            ) { eventListItem ->
                when (eventListItem) {
                    is EventItemData -> {
                        EventItem(
                            eventItemData = eventListItem,
                            onNavigateToEventEditor = onNavigateToEventEditor
                        )
                    }
                    is EventListDelimiter -> {
                        // TODO show delimiter
                    }
                }
            }
        }
    }
}