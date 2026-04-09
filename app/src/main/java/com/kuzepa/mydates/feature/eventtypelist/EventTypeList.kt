package com.kuzepa.mydates.feature.eventtypelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.ListScreenWithTopBar
import com.kuzepa.mydates.ui.components.rememberOnEvent
import com.kuzepa.mydates.ui.components.stateview.EmptyView
import com.kuzepa.mydates.ui.components.stateview.ErrorView
import com.kuzepa.mydates.ui.components.stateview.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTypeList(
    viewModel: EventTypeListViewModel = hiltViewModel(),
    onNavigateToEventTypeEditor: (id: String?) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLog: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    //val onEvent = viewModel.rememberOnEvent()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    ListScreenWithTopBar(
        title = stringResource(R.string.event_types_title),
        onNewItemFabClick = { onNavigateToEventTypeEditor(null) },
        newItemFabDescription = stringResource(R.string.event_type_creator_title),
        onNavigateBack = onNavigateBack,
        showGoBackDialog = false,
        onShowGoBackDialogChange = {},
        scrollBehavior = scrollBehavior
    ) {
        when (state) {
            is EventTypeListUiState.Loading -> {
                LoadingView()
            }

            is EventTypeListUiState.Success -> {
                val eventTypes = (state as EventTypeListUiState.Success).eventTypeList
                if (eventTypes.isEmpty()) {
                    EmptyView()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(rememberNestedScrollInteropConnection())
                            .padding(vertical = 2.dp)
                    ) {
                        items(
                            items = eventTypes,
                            key = { eventTypeListItem -> eventTypeListItem.id }
                        ) { eventTypeListItem ->
                            EventTypeItem(
                                eventType = eventTypeListItem,
                                onNavigateToEventTypeEditor = {
                                    onNavigateToEventTypeEditor(eventTypeListItem.id)
                                }
                            )
                        }
                    }
                }
            }

            is EventTypeListUiState.Error -> {
                val errorMessage = (state as EventTypeListUiState.Error).message
                ErrorView(
                    onContactSupport = { onNavigateToLog(errorMessage) },
                    errorMessage = errorMessage
                )
            }
        }
    }
}