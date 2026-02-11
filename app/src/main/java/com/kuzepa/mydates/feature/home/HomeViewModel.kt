package com.kuzepa.mydates.feature.home

import android.content.Context
import android.content.res.Resources
import android.icu.util.Calendar
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.common.util.month.getCurrentMonth
import com.kuzepa.mydates.common.util.month.getMonthNameList
import com.kuzepa.mydates.domain.formatter.toDateCode
import com.kuzepa.mydates.domain.model.MonthPager
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.feature.eventlist.EventItemData
import com.kuzepa.mydates.feature.eventlist.EventListGrouping
import com.kuzepa.mydates.ui.components.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val errorLoggerRepository: ErrorLoggerRepository,
    @param:ApplicationContext private val context: Context
) : BaseViewModel<HomeScreenEvent>() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Cache UI states per page
    private val _eventPageStates = mutableStateMapOf<Int, EventPageState>()
    val eventPageStates: SnapshotStateMap<Int, EventPageState> = _eventPageStates

    private val observedPages = mutableSetOf<Int>()

    // Flow observers for each page
    private val pageObservers = mutableMapOf<Int, Job>()

    private val todayDateCode = MutableStateFlow(Calendar.getInstance().toDateCode())

    init {
        setCurrentPage(getCurrentMonth())
        startDateUpdater()
    }

    fun startDateUpdater() {
        viewModelScope.launch {
            while (isActive) {
                delay(60_000) // check current date every minute
                val newDate = Calendar.getInstance().toDateCode()
                if (newDate != todayDateCode.value) {
                    todayDateCode.value = newDate
                    reloadAllPages()
                }
            }
        }
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
                        val errorMessage = getLogMessage(
                            tag = "EventPage",
                            title = "Error with getting events by month ${page + 1}",
                            throwable = e
                        )
                        errorLoggerRepository.logError(errorMessage)
                        _eventPageStates[page] = EventPageState.Error(errorMessage)
                    }
                }
                .collectLatest { events ->
                    val todayList = mutableListOf<EventItemData>()
                    val upcomingList = mutableListOf<EventItemData>()
                    val pastList = mutableListOf<EventItemData>()

                    events.forEach { event ->
                        val eventItemData = EventItemData.fromEvent(
                            event = event,
                            context,
                            // TODO get from preferences
                            isCompactAgeMode = true,
                            // TODO get from preferences
                            showZodiacSign = true
                        )
                        val eventDateCode =
                            event.notificationDateCode ?: event.date.toDateCode()
                        when {
                            eventDateCode == todayDateCode.value -> todayList.add(eventItemData)
                            eventDateCode > todayDateCode.value -> upcomingList.add(eventItemData)
                            else -> pastList.add(eventItemData)
                        }
                    }
                    _eventPageStates[page] = EventPageState.Success(
                        EventListGrouping(
                            today = todayList,
                            upcoming = upcomingList,
                            past = pastList
                        )
                    )
                }
        }
    }

    fun reloadPage(page: Int) {
        pageObservers[page]?.cancel()
        pageObservers.remove(page)
        observedPages.remove(page)
        observePage(page)
    }

    private fun reloadAllPages() {
        val pagesToReload = observedPages.toList()
        pagesToReload.forEach { page ->
            reloadPage(page)
        }
    }

    private fun stopObservingPage(page: Int) {
        pageObservers[page]?.cancel()
        pageObservers.remove(page)
        observedPages.remove(page)
        _eventPageStates.remove(page)
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
        _eventPageStates.clear()
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