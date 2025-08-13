package com.kuzepa.mydates.feature.home

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository
) : ViewModel() {
    private val _currentPage = MutableStateFlow(0)

    // Cache UI states per page
    private val _eventPageStates = mutableStateMapOf<Int, EventPageState>()
    val eventPageStates: SnapshotStateMap<Int, EventPageState> = _eventPageStates

    // Flow observers for each page
    private val pageObservers = mutableMapOf<Int, Job>()

    init {
        observePage(_currentPage.value)
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
        observePage(_currentPage.value)
    }

    fun observePage(page: Int) {
        // Cancel existing observer if any
        stopObservingPage(page)

        // Don't observe if already observing
        if (pageObservers.containsKey(page)) return

        // Set loading state immediately
        _eventPageStates[page] = EventPageState.Loading

        pageObservers[page] = viewModelScope.launch(Dispatchers.IO) {
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
    fun stopObservingPage(month: Int) {
        pageObservers[month]?.cancel()
        pageObservers.remove(month)
    }

    fun stopObservingAll() {
        pageObservers.values.forEach { it.cancel() }
        pageObservers.clear()
    }

    override fun onCleared() {
        stopObservingAll()
        super.onCleared()
    }

}