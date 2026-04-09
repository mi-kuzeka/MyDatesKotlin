package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.eventtype.EventTypeScreen
import com.kuzepa.mydates.feature.eventtypelist.EventTypeList
import kotlinx.serialization.Serializable

@Serializable
internal object EventTypes : NavRoute()

fun NavGraphBuilder.eventTypesDestination(
    onNavigateToEventTypeEditor: (id: String?) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLog: (String) -> Unit,
) {
    composable<EventTypes> {
        EventTypeList(
            onNavigateToEventTypeEditor = onNavigateToEventTypeEditor,
            onNavigateBack = onNavigateBack,
            onNavigateToLog = onNavigateToLog,
        )
    }
}

fun NavController.navigateToEventTypeEditor(id: String?) {
    navigate(route = EventTypeEditor(id = id))
}

@Serializable
internal data class EventTypeEditor(val id: String?) : NavRoute()

fun NavGraphBuilder.eventTypeEditorDestination(
    onNavigateBack: () -> Unit,
    onNavigateToLog: (errorMessage: String) -> Unit,
) {
    dialog<EventTypeEditor> { backStackEntry ->
        val eventTypeEditor: EventTypeEditor = backStackEntry.toRoute()
        EventTypeScreen(
            id = eventTypeEditor.id,
            onNavigateBack = onNavigateBack,
            onNavigateToLog = onNavigateToLog
        )
    }
}