package com.kuzepa.mydates.feature.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.stateview.EmptyView

@Composable
fun EventList(
    eventListGrouping: EventListGrouping,
    onNavigateToEventEditor: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (eventListGrouping.isEmpty()) {
        EmptyView(modifier)
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(rememberNestedScrollInteropConnection())
                .padding(vertical = 2.dp)
        ) {
            if (eventListGrouping.today.isNotEmpty()) {
                stickyHeader {
                    EventListSectionHeader(stringResource(R.string.today_events_header))
                }
                items(
                    items = eventListGrouping.today,
                    key = { eventListItem -> eventListItem.id }
                ) {eventListItem ->
                    EventItem(
                        eventItemData = eventListItem,
                        onNavigateToEventEditor = onNavigateToEventEditor
                    )
                }
            }
            if (eventListGrouping.past.isNotEmpty()) {
                stickyHeader {
                    EventListSectionHeader(stringResource(R.string.past_events_header))
                }
                items(
                    items = eventListGrouping.past,
                    key = { eventListItem -> eventListItem.id }
                ) {eventListItem ->
                    EventItem(
                        eventItemData = eventListItem,
                        onNavigateToEventEditor = onNavigateToEventEditor
                    )
                }
            }
            if (eventListGrouping.upcoming.isNotEmpty()) {
                stickyHeader {
                    EventListSectionHeader(stringResource(R.string.upcoming_events_header))
                }
                items(
                    items = eventListGrouping.upcoming,
                    key = { eventListItem -> eventListItem.id }
                ) {eventListItem ->
                    EventItem(
                        eventItemData = eventListItem,
                        onNavigateToEventEditor = onNavigateToEventEditor
                    )
                }
            }
        }
    }
}