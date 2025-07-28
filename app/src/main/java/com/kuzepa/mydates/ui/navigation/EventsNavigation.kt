package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuzepa.mydates.ui.activities.home.composable.HomeScreen
import com.kuzepa.mydates.ui.activities.home.event.composable.EventScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Home : TopLevelRoute()

fun NavGraphBuilder.eventsDestination(
    onNavigateToEventEditor: (id: Int?) -> Unit
) {
    composable<Home> {
        HomeScreen(onNavigateToEventEditor = onNavigateToEventEditor)
    }
}

fun NavController.navigateToEventEditor(id: Int?) {
    navigate(route = EventEditor(id = id))
}

@Serializable
internal data class EventEditor(val id: Int?) : NavRoute()

fun NavGraphBuilder.eventEditorDestination() {
    composable<EventEditor> { backStackEntry ->
        val eventEditor: EventEditor = backStackEntry.toRoute()
        EventScreen(id = eventEditor.id)
    }
}