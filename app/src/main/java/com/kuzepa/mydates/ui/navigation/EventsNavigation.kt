package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuzepa.mydates.ui.activities.home.composable.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Home : TopLevelRoute()

fun NavGraphBuilder.eventsDestination(
    onNavigateToEventEditor: (id: Int) -> Unit
) {
    composable<Home> {
        HomeScreen(onNavigateToEventEditor = onNavigateToEventEditor)
    }
}

fun NavController.navigateToEventEditor(id: Int) {
    navigate(route = EditEvent(id = id))
}

fun NavController.navigateToEventCreator() {
    navigate(route = AddEvent)
}


@Serializable
internal object AddEvent : NavRoute()

fun NavGraphBuilder.eventCreatorDestination() {
    composable<AddEvent> {
//                AddEventScreen(navController)
    }
}

@Serializable
internal data class EditEvent(val id: Int) : NavRoute()

fun NavGraphBuilder.eventEditorDestination() {
    composable<EditEvent> { backStackEntry ->
        val editEvent: EditEvent = backStackEntry.toRoute()
        // TODO pass this argument directly to the EditEventScreen
        val id = editEvent.id
//                EditEventScreen()
    }
}