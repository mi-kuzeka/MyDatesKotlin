package com.kuzepa.mydates.feature.home

sealed class HomeScreenEvent {
    data class UpdateCurrentPage(val currentPage: Int) : HomeScreenEvent()
    data class ObservePage(val page: Int) : HomeScreenEvent()
    data class StopObservingPagesOutsideRange(val preloadRange: IntRange) : HomeScreenEvent()
    data class OnEventNavigationResult(val resultMonth: Int) :
        HomeScreenEvent()
}