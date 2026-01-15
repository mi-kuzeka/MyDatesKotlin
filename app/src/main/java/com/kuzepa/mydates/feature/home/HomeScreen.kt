package com.kuzepa.mydates.feature.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.domain.model.MonthPager
import com.kuzepa.mydates.feature.eventlist.EventList
import com.kuzepa.mydates.feature.home.components.CustomTabs
import com.kuzepa.mydates.ui.components.rememberOnEvent
import com.kuzepa.mydates.ui.components.stateview.EmptyView
import com.kuzepa.mydates.ui.components.stateview.ErrorView
import com.kuzepa.mydates.ui.components.stateview.LoadingView
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToEventEditor: (Long) -> Unit,
    onMonthChanged: ((Int) -> Unit) -> Unit,
    modifier: Modifier
) {
    val configuration = LocalConfiguration.current
    val resources = LocalResources.current
    val tabs = remember(configuration) { viewModel.getTabNames(resources) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = viewModel.rememberOnEvent()
    val eventPageStates = remember(viewModel.eventPageStates) { viewModel.eventPageStates }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { MonthPager.PAGE_COUNT }
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onEvent(HomeScreenEvent.UpdateCurrentPage(page))
            }
    }

    LaunchedEffect(state.currentPage) {
        val currentPage = state.currentPage
        if (pagerState.currentPage == currentPage) return@LaunchedEffect

        pagerState.scrollToPage(currentPage)
        val preloadRange = max(
            MonthPager.FIRST_PAGE,
            currentPage - MonthPager.PRELOAD_PAGES_COUNT
        )..min(
            MonthPager.LAST_PAGE,
            currentPage + MonthPager.PRELOAD_PAGES_COUNT
        )

        // Stop observing pages outside of preload range
        onEvent(HomeScreenEvent.StopObservingPagesOutsideRange(preloadRange))

        // Start observing new pages in preload range
        preloadRange.forEach { page ->
            onEvent(HomeScreenEvent.ObservePage(page))
        }
    }

    LaunchedEffect(onMonthChanged) {
        onMonthChanged({ onEvent(HomeScreenEvent.OnEventNavigationResult(it)) })
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        CustomTabs(
            tabs,
            pagerState.currentPage,
            updateCurrentPage = { onEvent(HomeScreenEvent.UpdateCurrentPage(it)) },
            coroutineScope,
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                ),
            beyondViewportPageCount = MonthPager.PRELOAD_PAGES_COUNT,
            key = { page -> page },  // This ensures proper page identity
        ) { pageIndex ->
            val pageState = remember(eventPageStates[pageIndex]) { eventPageStates[pageIndex] }

            when (pageState) {
                null -> {
                    if (abs(pageIndex - pagerState.currentPage) <= MonthPager.PRELOAD_PAGES_COUNT) {
                        LaunchedEffect(pageIndex) {
                            onEvent(HomeScreenEvent.ObservePage(pageIndex))
                        }
                        LoadingView()
                    } else {
                        EmptyView()
                    }
                }

                is EventPageState.Loading -> {
                    LoadingView()
                }

                is EventPageState.Error -> ErrorView(
                    message = pageState.message,
                    onRetry = { onEvent(HomeScreenEvent.ObservePage(pageIndex)) }
                )

                is EventPageState.Success -> {
                    EventList(
                        eventListGrouping = pageState.eventListGrouping,
                        onNavigateToEventEditor = onNavigateToEventEditor
                    )
                }
            }
        }
    }
}