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
    onNavigateToEventTypeEditor: (id: String) -> Unit
) {
    composable<EventTypes> {
//        EventTypesScreen(onNavigateToEventTypeEditor = onNavigateToEventTypeEditor)
    }
}

fun NavController.navigateToEventTypeEditor(id: String) {
    navigate(route = EditEventType(id = id))
}

@Serializable
internal object AddEventType : NavRoute()

fun NavGraphBuilder.eventTypeCreatorDestination() {
    dialog<AddEventType> {
//                AddEventTypeScreen()
    }
}

@Serializable
internal data class EditEventType(val id: String) : NavRoute()

fun NavGraphBuilder.eventTypeEditorDestination() {
    dialog<EditEventType> { backStackEntry ->
        val editEventType: EditEventType = backStackEntry.toRoute()
        // TODO pass this argument directly to the EditEventTypeScreen
        val id = editEventType.id
//                EditEventTypeScreen()
    }
}