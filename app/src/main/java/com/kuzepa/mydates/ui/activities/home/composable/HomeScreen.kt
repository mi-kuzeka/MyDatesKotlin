package com.kuzepa.mydates.ui.activities.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.ui.activities.home.HomeUiState
import com.kuzepa.mydates.ui.activities.home.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToEventEditor: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    //TODO fill with real data
    val tabs =
        listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 12 }

    Column {
        CustomTabs(
            tabs,
            pagerState,
            { viewModel.setCurrentPage(it) },
            coroutineScope
        )

        HorizontalPager(state = pagerState) { page ->
            when (uiState) {
                HomeUiState.Loading -> LoadingIndicator()
                HomeUiState.Error -> ErrorScreen()
                is HomeUiState.Success -> EventList(
                    events = (uiState as HomeUiState.Success).events,
                    onNavigateToEventEditor = onNavigateToEventEditor
                )
            }
        }
    }
}

@Composable
fun EventList(
    events: List<Event>,
    onNavigateToEventEditor: (Int) -> Unit,
) {
    LazyColumn {
        items(events) { event ->
            EventItem(
                event = event,
                onNavigateToEventEditor = onNavigateToEventEditor
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onNavigateToEventEditor: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onNavigateToEventEditor(event.id) })
    ) {
        if (event.image == null) {
            // TODO show image from resources
        } else {
            Image(bitmap = event.image.asImageBitmap(), contentDescription = null)
        }
        Column {
            Text(event.name)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // TODO replace this text with string resource
        Text("Error loading events", color = MaterialTheme.colorScheme.error)
    }
}