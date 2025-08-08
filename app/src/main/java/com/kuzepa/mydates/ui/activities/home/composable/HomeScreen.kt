package com.kuzepa.mydates.ui.activities.home.composable

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.ui.activities.home.EventPageState
import com.kuzepa.mydates.ui.activities.home.HomeViewModel
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onNavigateToEventEditor: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val firstPage = 0
    val lastPage = 11
    val beyondViewportPageCount = 2
    //TODO fill with real data from resources
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
    // TODO replace firstPage with current month
    val pagerState = rememberPagerState(initialPage = firstPage, pageCount = { 12 })
    val coroutineScope = rememberCoroutineScope()

    // Track current active state
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

    val observedPages = remember { mutableStateSetOf<Int>() }

    // Preload adjacent pages
    LaunchedEffect(pagerState.currentPage, lifecycleState) {
        val current = pagerState.currentPage

        val preloadRange = max(firstPage, current - beyondViewportPageCount)..min(
            lastPage,
            current + beyondViewportPageCount
        )

        // Stop observing pages outside of preload range
        (observedPages.toSet() - preloadRange.toSet()).forEach { page ->
            viewModel.stopObservingPage(page)
            observedPages.remove(page)
        }

        // Start observing new pages in preload range
        if (lifecycleState.isAtLeast(Lifecycle.State.STARTED)) {
            preloadRange.forEach { page ->
                if (page !in observedPages) {
                    viewModel.observePage(page)
                    observedPages.add(page)
                }
            }
        }
    }

    // Handle background/foreground transitions
    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                viewModel.stopObservingAll()
                observedPages.clear()
            }

            override fun onStart(owner: LifecycleOwner) {
                observedPages.forEach { page ->
                    viewModel.observePage(page)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column {
        CustomTabs(
            tabs,
            pagerState,
            updateCurrentPage = { viewModel.setCurrentPage(it) },
            coroutineScope
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = beyondViewportPageCount,
            key = { page -> page },  // This ensures proper page identity
        ) { pageIndex ->
            val state = viewModel.eventPageStates[pageIndex]

            // Handle initial state
            if (state == null) {
                // Show loading only if we should be loading this page
                if (pageIndex in observedPages) {
                    LoadingView()
                } else {
                    // Show empty state if not yet loaded
                    EmptyView()
                }
            } else {
                val state = viewModel.eventPageStates[pageIndex] ?: EventPageState.Loading
                when (state) {
                    EventPageState.Loading -> LoadingView()
                    is EventPageState.Error -> ErrorView(
                        message = state.message,
                        onRetry = {
                            viewModel.observePage(pageIndex)
                        }
                    )

                    is EventPageState.Success -> {
                        EventList(
                            events = state.events,
                            onNavigateToEventEditor = onNavigateToEventEditor,
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
    onNavigateToEventEditor: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        if (events.isEmpty()) {
            EmptyView()
        } else {
            LazyColumn {
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
}

@Composable
fun EventItem(
    event: Event?,
    onNavigateToEventEditor: (Int) -> Unit,
) {
    event?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
}

@Composable
fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO add button "Send report" with error message instead of showing this text for the user
        Text("Error: $message", color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry") // TODO replace with string resource
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // TODO replace with actual empty view
        Text("No events yet", style = MaterialTheme.typography.bodyMedium)
    }
}