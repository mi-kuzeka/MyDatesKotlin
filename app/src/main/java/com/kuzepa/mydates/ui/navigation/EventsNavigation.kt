package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.eventlist.event.EventScreen
import com.kuzepa.mydates.feature.home.HomeScreen
import com.kuzepa.mydates.feature.imagecropper.ImageCropperScreen
import com.kuzepa.mydates.ui.components.ScreenWithBottomBar
import kotlinx.serialization.Serializable

@Serializable
internal object Home : TopLevelRoute()

fun NavGraphBuilder.eventsDestination(
    navController: NavHostController,
    onNavigateToEventEditor: (id: Long?) -> Unit,
    onNavigateToLogs: (errorMessage: String) -> Unit
) {
    composable<Home> { backStackEntry ->
        val savedStateHandle = backStackEntry.savedStateHandle
        val month = savedStateHandle.get<Int?>(NavigationResult.EVENT_MONTH_KEY)

        ScreenWithBottomBar(navController) { modifier ->
            HomeScreen(
                onNavigateToEventEditor = onNavigateToEventEditor,
                onMonthChanged = { onEvent ->
                    month?.let {
                        onEvent(it)
                        savedStateHandle.remove<Int>(NavigationResult.EVENT_KEY)
                        savedStateHandle.remove<String?>(NavigationResult.EVENT_MONTH_KEY)
                    }
                },
                onNavigateToLogsScreen = onNavigateToLogs,
                modifier = modifier
            )
        }
    }
}

fun NavController.navigateToEventEditor(id: Long?) {
    navigate(route = EventEditor(id = id))
}

@Serializable
internal data class EventEditor(val id: Long?) : NavRoute()

fun NavGraphBuilder.eventEditorDestination(
    onNavigateBack: (result: Int, eventMonth: Int?) -> Unit,
    onNavigateToEventTypeCreator: () -> Unit,
    onNavigateToLabelChooser: (eventLabelIdsJson: String) -> Unit,
    onNavigateToLabelEditor: (id: String?, fromEvent: Boolean, showDeleteButton: Boolean) -> Unit,
    onNavigateToImageCropper: (imageUriString: String) -> Unit,
    onNavigateToLog: (errorMessage: String) -> Unit,
) {
    composable<EventEditor> { backStackEntry ->
        val eventEditor: EventEditor = backStackEntry.toRoute()
        EventScreen(
            eventId = eventEditor.id,
            onNavigateBack = onNavigateBack,
            onNavigateToEventTypeCreator = onNavigateToEventTypeCreator,
            onNavigateToLabelChooser = onNavigateToLabelChooser,
            onNavigateToLabelEditor = { onNavigateToLabelEditor(it, true, false) },
            onNavigateToImageCropper = onNavigateToImageCropper,
            onNavigateToLog = onNavigateToLog
        )
    }
}

fun NavController.navigateToImageCropper(imageUriString: String) {
    navigate(route = ImageCropper(imageUriString = imageUriString))
}

@Serializable
internal data class ImageCropper(val imageUriString: String) : NavRoute()

fun NavGraphBuilder.imageCropperDestination(
    onNavigateBack: () -> Unit,

    ) {
    composable<ImageCropper> { backStackEntry ->
        val imageCropper: ImageCropper = backStackEntry.toRoute()

        ImageCropperScreen(
            imageUriString = imageCropper.imageUriString,
            onNavigateBack = onNavigateBack
        )
    }
}