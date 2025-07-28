package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
internal object EventTypes : NavRoute()

fun NavGraphBuilder.eventTypesDestination(
    onNavigateToEventTypeEditor: (id: String?) -> Unit
) {
    composable<EventTypes> {
//        EventTypesScreen(onNavigateToEventTypeEditor = onNavigateToEventTypeEditor)
    }
}

fun NavController.navigateToEventTypeEditor(id: String?) {
    navigate(route = EventTypeEditor(id = id))
}

@Serializable
internal data class EventTypeEditor(val id: String?) : NavRoute()

fun NavGraphBuilder.eventTypeEditorDestination() {
    dialog<EventTypeEditor> { backStackEntry ->
        val eventTypeEditor: EventTypeEditor = backStackEntry.toRoute()
//                EventTypeEditorScreen(id = eventTypeEditor.id)
    }
}