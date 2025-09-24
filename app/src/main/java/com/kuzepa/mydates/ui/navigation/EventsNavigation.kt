package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.home.HomeScreen
import com.kuzepa.mydates.feature.home.event.EventScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Home : TopLevelRoute()

fun NavGraphBuilder.eventsDestination(
    onNavigateToEventEditor: (id: Long?) -> Unit
) {
    composable<Home> {
        HomeScreen(onNavigateToEventEditor = onNavigateToEventEditor)
    }
}

fun NavController.navigateToEventEditor(id: Long?) {
    navigate(route = EventEditor(id = id))
}

@Serializable
internal data class EventEditor(val id: Long?) : NavRoute()

fun NavGraphBuilder.eventEditorDestination(
    onNavigateBack: () -> Unit,
    onNavigateToEventTypeCreator: () -> Unit,
    onNavigateToLabelChooser: () -> Unit,
    onNavigateToLabelEditor: (id: String?) -> Unit,
) {
    composable<EventEditor> { backStackEntry ->
        val eventEditor: EventEditor = backStackEntry.toRoute()
        val savedStateHandle = backStackEntry.savedStateHandle

        val eventTypeNavigationResult =
            savedStateHandle.get<Int>(NavigationResult.EVENT_TYPE_KEY)
        val eventTypeId = savedStateHandle.get<String?>(NavigationResult.EVENT_TYPE_ID_KEY)
        val labelNavigationResult =
            savedStateHandle.get<Int>(NavigationResult.LABEL_KEY)
        val labelId = savedStateHandle.get<String?>(NavigationResult.LABEL_ID_KEY)

        EventScreen(
            eventId = eventEditor.id,
            onNavigateBack = onNavigateBack,
            onNavigateToEventTypeCreator = onNavigateToEventTypeCreator,
            onNavigateToLabelChooser = onNavigateToLabelChooser,
            onNavigateToLabelEditor = onNavigateToLabelEditor,
            eventTypeNavigationResult = NavigationResultData(
                result = eventTypeNavigationResult,
                id = eventTypeId
            ),
            labelNavigationResult = NavigationResultData(
                result = labelNavigationResult,
                id = labelId
            ),
            removeNavigationResult = { navigationKey ->
                when (navigationKey) {
                    NavigationResult.EVENT_TYPE_KEY -> {
                        savedStateHandle.remove<Int>(NavigationResult.EVENT_TYPE_KEY)
                        savedStateHandle.remove<String?>(NavigationResult.EVENT_TYPE_ID_KEY)
                    }

                    NavigationResult.LABEL_KEY -> {
                        savedStateHandle.remove<Int>(NavigationResult.LABEL_KEY)
                        savedStateHandle.remove<String?>(NavigationResult.LABEL_ID_KEY)
                    }
                }
            }
        )
    }
}