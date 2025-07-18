package com.kuzepa.mydates.ui.activities.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.ui.activities.main.MainUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    uiState: MainUiState,
    updateCurrentPage: (Int) -> Unit,
    addEvent: () -> Unit
//    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = { EventFab(addEvent, coroutineScope) },
        topBar = { ControlPanel() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
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
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    tabs.forEachIndexed { i, title ->
                        Tab(selected = pagerState.currentPage == i, onClick = {
                            coroutineScope.launch {
                                updateCurrentPage(i)
                                pagerState.animateScrollToPage(i)
                            }
                        }) {
                            Text(title)
                        }
                    }
                }

                HorizontalPager(state = pagerState) { page ->
                    when (uiState) {
                        MainUiState.Loading -> LoadingIndicator(innerPadding)
                        MainUiState.Error -> ErrorScreen(innerPadding)
                        is MainUiState.Success -> EventList(
                            events = uiState.events,
                            paddingValues = innerPadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventList(
    events: List<Event>,
    paddingValues: PaddingValues
) {
    LazyColumn(contentPadding = paddingValues) {
        items(events) { event ->
            EventItem(event = event)
        }
    }
}

@Composable
fun EventItem(
    event: Event
) {
    Row {
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
fun LoadingIndicator(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        // TODO replace this text with string resource
        Text("Error loading events", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun EventFab(addEvent: () -> Unit, coroutineScope: CoroutineScope) {
    FloatingActionButton(onClick = {
        coroutineScope.launch {
            addEvent()
        }
//        navController.navigate("event_creation")
    }) { /* ... */ }
}

@Composable
fun ControlPanel() {
    Row {
        Text("There should be a panel")
    }
    //TODO
}