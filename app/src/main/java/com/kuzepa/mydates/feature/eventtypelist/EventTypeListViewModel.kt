package com.kuzepa.mydates.feature.eventtypelist

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.ui.components.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventTypeListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    eventTypeRepository: EventTypeRepository,
    private val errorLoggerRepository: ErrorLoggerRepository,
) : BaseViewModel<EventTypeListEvent>() {

    val logTag = "EventTypeListViewModel"
    val uiState: StateFlow<EventTypeListUiState> = eventTypeRepository.getAllEventTypes()
        .map<List<EventType>, EventTypeListUiState> { eventTypes ->
            EventTypeListUiState.Success(eventTypes)
        }.catch { e ->
            if (e is CancellationException) throw e
            onError(
                logMessage = getLogMessage(
                    logTag,
                    "Error getting event types",
                    e
                )
            )
            emit(
                EventTypeListUiState.Error(
                    context.resources.getString(R.string.error_getting_event_types)
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EventTypeListUiState.Loading
        )

    override fun onEvent(event: EventTypeListEvent) {

    }

    override suspend fun onError(logMessage: String, showingMessage: String?) {
        withContext((Dispatchers.IO)) {
            errorLoggerRepository.logError(logMessage)
        }
    }
}