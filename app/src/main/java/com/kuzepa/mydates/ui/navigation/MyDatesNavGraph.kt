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
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        eventsDestination(
            navController = navController,
            onNavigateToEventEditor = navigationActions::navigateToEventEditor,
            onNavigateToLogs = navigationActions::navigateToLogs
        )
        eventEditorDestination(
            onNavigateBack = navigationActions::onNavigateBackFromEventEditor,
            onNavigateToEventTypeCreator = navigationActions::navigateToEventTypeCreator,
            onNavigateToLabelChooser = navigationActions::navigateToLabelChooser,
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor,
            onNavigateToImageCropper = navigationActions::navigateToImageCropper,
            onNavigateToLog = navigationActions::navigateToLogs
        )
        imageCropperDestination(onNavigateBack = navigationActions::onNavigateBack)

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
            onNavigateBack = navigationActions::onNavigateBack
        )
        eventTypeEditorDestination(
            onNavigateBack = navigationActions::onNavigateBack
        )

        labelsDestination(
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor,
            onNavigateToBulkLabelAssignment = { labelId ->
                navController.navigateToBulkLabelAssignment(labelId)
            },
            onNavigateBack = navigationActions::onNavigateBack
        )
        labelEditorDestination(
            onNavigateBack = navigationActions::onNavigateBack,
            onNavigateToColorPicker = navigationActions::navigateToColorPicker
        )
        colorPickerDestination(
            onNavigateBack = navigationActions::onNavigateBack
        )
        labelChooserDestination(
            onNavigateBack = navigationActions::onNavigateBack,
            onNavigateToLabelEditor = navigationActions::navigateToLabelEditor
        )
        bulkLabelAssignmentDestination(onNavigateBack = navigationActions::onNavigateBack)

        dataTransferDestination(
            onNavigateToContactsImport = { navController.navigateToContactsImport() },
            onNavigateToCsvImport = { navController.navigateToCsvImport() },
            onNavigateToCsvExport = { navController.navigateToCsvExport() },
            onNavigateBack = navigationActions::onNavigateBack
        )
        contactsImportDestination(onNavigateBack = navigationActions::onNavigateBack)
        csvImportDestination(onNavigateBack = navigationActions::onNavigateBack)
        csvExportDestination(onNavigateBack = navigationActions::onNavigateBack)

        settingsDestination(
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onNavigateToNotificationFilter = { navController.navigateToNotificationFilter() },
            onNavigateBack = navigationActions::onNavigateBack
        )
        notificationSettingsDestination(onNavigateBack = navigationActions::onNavigateBack)
        notificationFilterDestination(onNavigateBack = navigationActions::onNavigateBack)

        donationDestination(onNavigateBack = navigationActions::onNavigateBack)

        helpDestination(
            onNavigateToNotificationTroubleshoot =
                { navController.navigateToNotificationTroubleshoot() },
            onNavigateBack = navigationActions::onNavigateBack
        )
        notificationTroubleshootDestination(onNavigateBack = navigationActions::onNavigateBack)

        aboutDestination(
            onNavigateToThirdPartyLibraries = { navController.navigateToThirdPartyLibraries() },
            onNavigateBack = navigationActions::onNavigateBack
        )

        logsDestination(
            onNavigateBack = navigationActions::onNavigateBack
        )
    }
}

@Stable
class NavigationActions(private val navController: NavHostController) {
    fun navigateToEventEditor(eventId: Long?) {
        navController.navigateToEventEditor(id = eventId)
    }

    fun onNavigateBackFromEventEditor(result: Int, eventMonth: Int?) {
        onNavigateBackWithResult(
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

    fun navigateToLogs(errorMessage: String) {
        navController.navigateToLogs(errorMessage)
    }

    fun onNavigateBack() = navController.popBackStack()

    private fun <T> onNavigateBackWithResult(
        resultKey: String,
        result: Int,
        dataKey: String,
        data: T
    ) {
        onNavigateBack()
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