package com.kuzepa.mydates.feature.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.domain.model.MonthPager
import com.kuzepa.mydates.feature.home.components.CustomTabs
import com.kuzepa.mydates.ui.components.list.eventlist.EventList
import com.kuzepa.mydates.ui.components.list.state.EmptyView
import com.kuzepa.mydates.ui.components.list.state.ErrorView
import com.kuzepa.mydates.ui.components.list.state.LoadingView
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onNavigateToEventEditor: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
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
    val pagerState = rememberPagerState(
        initialPage = MonthPager.FIRST_PAGE,
        pageCount = { MonthPager.PAGE_COUNT }
    )
    val coroutineScope = rememberCoroutineScope()

    // Track current active state
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

    val observedPages = remember { mutableStateSetOf<Int>() }

    // Preload adjacent pages
    LaunchedEffect(pagerState.currentPage, lifecycleState) {
        val current = pagerState.currentPage

        val preloadRange = max(
            MonthPager.FIRST_PAGE,
            current - MonthPager.PRELOAD_PAGES_COUNT
        )..min(
            MonthPager.LAST_PAGE,
            current + MonthPager.PRELOAD_PAGES_COUNT
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
            beyondViewportPageCount = MonthPager.PRELOAD_PAGES_COUNT,
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
                            onNavigateToEventEditor = onNavigateToEventEditor
                        )
                    }
                }
            }
        }
    }
}