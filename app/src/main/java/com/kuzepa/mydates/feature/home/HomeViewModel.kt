package com.kuzepa.mydates.feature.home

import android.content.res.Resources
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.common.util.month.getCurrentMonth
import com.kuzepa.mydates.common.util.month.getMonthNameList
import com.kuzepa.mydates.domain.model.MonthPager
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.ui.components.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
) : BaseViewModel<HomeScreenEvent>() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Cache UI states per page
    private val _eventPageStates = mutableStateMapOf<Int, EventPageState>()
    val eventPageStates: SnapshotStateMap<Int, EventPageState> = _eventPageStates

    private val observedPages = mutableSetOf<Int>()

    // Flow observers for each page
    private val pageObservers = mutableMapOf<Int, Job>()

    init {
        setCurrentPage(getCurrentMonth())
    }

    fun getTabNames(resources: Resources): List<String> = getMonthNameList(resources)

    private fun setCurrentPage(page: Int) {
        val newPage = when {
            page < MonthPager.FIRST_PAGE -> MonthPager.FIRST_PAGE
            page > MonthPager.LAST_PAGE -> MonthPager.LAST_PAGE
            else -> page
        }
        if (_uiState.value.currentPage != newPage)
            _uiState.update { it.copy(currentPage = newPage) }
    }

    private fun observePage(page: Int) {
        // Prevent duplicate observation
        if (page in observedPages) return

        observedPages.add(page)
        // Set loading state immediately
        _eventPageStates[page] = EventPageState.Loading

        pageObservers[page] = viewModelScope.launch {
            eventRepository.getEventsByMonth(page + 1, SortOption.BY_DATE_DESC)
                .distinctUntilChanged()
                .catch { e ->
                    if (isActive) {
                        _eventPageStates[page] = EventPageState.Error(e.message ?: "Unknown error")
                    }
                }
                .collect { events ->
                    _eventPageStates[page] = EventPageState.Success(events)
                }
        }
    }

    private fun stopObservingPage(page: Int) {
        pageObservers[page]?.cancel()
        pageObservers.remove(page)
        observedPages.remove(page)
    }

    private fun stopObservingPagesOutsideRange(validRange: IntRange) {
        val pagesToRemove = observedPages.filter { it !in validRange }
        pagesToRemove.forEach { page ->
            stopObservingPage(page)
        }
    }

    private fun stopObservingAll() {
        pageObservers.values.forEach { it.cancel() }
        pageObservers.clear()
        observedPages.clear()
    }

    override fun onCleared() {
        stopObservingAll()
        super.onCleared()
    }

    override fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.UpdateCurrentPage -> {
                setCurrentPage(event.currentPage)
            }

            is HomeScreenEvent.ObservePage -> {
                observePage(event.page)
            }

            is HomeScreenEvent.StopObservingPagesOutsideRange -> {
                stopObservingPagesOutsideRange(event.preloadRange)
            }

            is HomeScreenEvent.OnEventNavigationResult -> {
                setCurrentPage(event.resultMonth - 1)
            }
        }
    }
}