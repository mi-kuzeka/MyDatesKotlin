package com.kuzepa.mydates.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MyDatesNavHost(
    navController: NavHostController
) {
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Home,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        eventsDestination(
            navController = navController,
            onNavigateToEventEditor = navigationActions::navigateToEventEditor,
            onNavigateToLog = navigationActions::navigateToLog
        )
        eventEditorDestination(
            onNavigateBack = navigationActions::navigateBackFromEventEditor,
            onNavigateToEventTypeCreator = navigationActions::navigateToEventTypeCreator,
            onNavigateToLabelChooser = navigationActions::navigateToLabelChooser,
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor,
            onNavigateToImageCropper = navigationActions::navigateToImageCropper,
            onNavigateToLog = navigationActions::navigateToLog
        )
        imageCropperDestination(
            onNavigateBack = navigationActions::navigateBack,
            onNavigateToLog = navigationActions::navigateToLog
        )

        appearanceDestination(navController = navController)

        searchDestination(navController = navController)

        moreDestination(
            navController = navController,
            onNavigateToEventTypes = { navController.navigateToEventTypes() },
            onNavigateToLabels = { navController.navigateToLabels() },
            onNavigateToDataTransfer = { navController.navigateToDataTransfer() },
            onNavigateToSettings = { navController.navigateToSettings() },
            onNavigateToDonation = { navController.navigateToDonation() },
            onNavigateToHelp = { navController.navigateToHelp() },
            onNavigateToAbout = { navController.navigateToAbout() },
        )
        eventTypesDestination(
            onNavigateToEventTypeEditor = navigationActions::navigateToEventTypeEditor,
            onNavigateBack = navigationActions::navigateBack,
            onNavigateToLog = navigationActions::navigateToLog
        )
        eventTypeEditorDestination(
            onNavigateBack = navigationActions::navigateBack,
            onNavigateToLog = navigationActions::navigateToLog
        )

        labelsDestination(
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor,
            onNavigateToBulkLabelAssignment = { labelId ->
                navController.navigateToBulkLabelAssignment(labelId)
            },
            onNavigateBack = navigationActions::navigateBack
        )
        labelEditorDestination(
            onNavigateBack = navigationActions::navigateBack,
            onNavigateToColorPicker = navigationActions::navigateToColorPicker,
            onNavigateToLog = navigationActions::navigateToLog
        )
        colorPickerDestination(
            onNavigateBack = navigationActions::navigateBack
        )
        labelChooserDestination(
            onNavigateBack = navigationActions::navigateBack,
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor,
            onNavigateToLog = navigationActions::navigateToLog
        )
        bulkLabelAssignmentDestination(onNavigateBack = navigationActions::navigateBack)

        dataTransferDestination(
            onNavigateToContactsImport = { navController.navigateToContactsImport() },
            onNavigateToCsvImport = { navController.navigateToCsvImport() },
            onNavigateToCsvExport = { navController.navigateToCsvExport() },
            onNavigateBack = navigationActions::navigateBack
        )
        contactsImportDestination(onNavigateBack = navigationActions::navigateBack)
        csvImportDestination(onNavigateBack = navigationActions::navigateBack)
        csvExportDestination(onNavigateBack = navigationActions::navigateBack)

        settingsDestination(
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onNavigateToNotificationFilter = { navController.navigateToNotificationFilter() },
            onNavigateBack = navigationActions::navigateBack
        )
        notificationSettingsDestination(onNavigateBack = navigationActions::navigateBack)
        notificationFilterDestination(onNavigateBack = navigationActions::navigateBack)

        donationDestination(onNavigateBack = navigationActions::navigateBack)

        helpDestination(
            onNavigateToNotificationTroubleshoot =
                { navController.navigateToNotificationTroubleshoot() },
            onNavigateBack = navigationActions::navigateBack
        )
        notificationTroubleshootDestination(onNavigateBack = navigationActions::navigateBack)

        aboutDestination(
            onNavigateToThirdPartyLibraries = { navController.navigateToThirdPartyLibraries() },
            onNavigateBack = navigationActions::navigateBack
        )

        logDestination(
            onNavigateBack = navigationActions::navigateBack
        )
    }
}

@Stable
class NavigationActions(private val navController: NavHostController) {
    fun navigateToEventEditor(eventId: Long?) {
        navController.navigateToEventEditor(id = eventId)
    }

    fun navigateBackFromEventEditor(result: Int, eventMonth: Int?) {
        navigateBackWithResult(
            resultKey = NavigationResult.EVENT_KEY,
            result = result,
            dataKey = NavigationResult.EVENT_MONTH_KEY,
            data = eventMonth
        )
    }

    fun navigateToImageCropper(imageUriString: String) {
        navController.navigateToImageCropper(imageUriString = imageUriString)
    }

    fun navigateToEventTypeCreator() {
        navigateToEventTypeEditor(null)
    }

    fun navigateToEventTypeEditor(eventTypeId: String?) {
        navController.navigateToEventTypeEditor(eventTypeId)
    }

    fun navigateToLabelChooser(eventLabelIdsJson: String) {
        navController.navigateToLabelChooser(eventLabelIdsJson)
    }

    fun navigateToLabelEditor(labelId: String?, fromEvent: Boolean, showDeleteButton: Boolean) {
        navController.navigateToLabelEditor(
            id = labelId,
            isOpenedFromEvent = fromEvent,
            showDeleteButton = showDeleteButton
        )
    }

    fun navigateToColorPicker(color: Int?) {
        navController.navigateToColorPicker(color)
    }

    fun navigateToLog(errorMessage: String) {
        navController.navigateToLog(errorMessage)
    }

    fun navigateBack() = navController.popBackStack()

    private fun <T> navigateBackWithResult(
        resultKey: String,
        result: Int,
        dataKey: String,
        data: T
    ) {
        navigateBack()
        if (result == NavigationResult.OK) {
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.apply {
                    set(resultKey, result)
                    data?.let { set(dataKey, it) }
                }
        }
    }
}