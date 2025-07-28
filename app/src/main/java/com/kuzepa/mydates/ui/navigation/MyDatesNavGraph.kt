package com.kuzepa.mydates.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MyDatesNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        eventsDestination(onNavigateToEventEditor = { eventId ->
            navController.navigateToEventEditor(id = eventId)
        })
        eventEditorDestination()

        appearanceDestination()

        searchDestination()

        moreDestination(
            onNavigateToEventTypes = { navController.navigateToEventTypes() },
            onNavigateToLabels = { navController.navigateToLabels() },
            onNavigateToDataTransfer = { navController.navigateToDataTransfer() },
            onNavigateToSettings = { navController.navigateToSettings() },
            onNavigateToDonation = { navController.navigateToDonation() },
            onNavigateToHelp = { navController.navigateToHelp() },
            onNavigateToAbout = { navController.navigateToAbout() },
        )
        eventTypesDestination(onNavigateToEventTypeEditor = { eventTypeId ->
            navController.navigateToEventTypeEditor(eventTypeId)
        })
        eventTypeEditorDestination()

        labelsDestination(
            onNavigateToLabelEditor = { labelId ->
                navController.navigateToLabelEditor(labelId)
            },
            onNavigateToBulkLabelAssignment = { labelId ->
                navController.navigateToBulkLabelAssignment(labelId)
            })
        labelEditorDestination()
        bulkLabelAssignmentDestination()

        dataTransferDestination(
            onNavigateToContactsImport = { navController.navigateToContactsImport() },
            onNavigateToCsvImport = { navController.navigateToCsvImport() },
            onNavigateToCsvExport = { navController.navigateToCsvExport() }
        )
        contactsImportDestination()
        csvImportDestination()
        csvExportDestination()

        settingsDestination(
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onNavigateToNotificationFilter = { navController.navigateToNotificationFilter() }
        )
        notificationSettingsDestination()
        notificationFilterDestination()

        donationDestination()

        helpDestination(
            onNavigateToNotificationTroubleshoot =
                { navController.navigateToNotificationTroubleshoot() })
        notificationTroubleshootDestination()

        aboutDestination(
            onNavigateToThirdPartyLibraries = { navController.navigateToThirdPartyLibraries() }
        )
    }
}