package com.kuzepa.mydates.ui.activities.main.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.navigation.Appearance
import com.kuzepa.mydates.ui.navigation.Home
import com.kuzepa.mydates.ui.navigation.More
import com.kuzepa.mydates.ui.navigation.Search
import com.kuzepa.mydates.ui.navigation.aboutDestination
import com.kuzepa.mydates.ui.navigation.appearanceDestination
import com.kuzepa.mydates.ui.navigation.bulkLabelAssignmentDestination
import com.kuzepa.mydates.ui.navigation.contactsImportDestination
import com.kuzepa.mydates.ui.navigation.containsRoute
import com.kuzepa.mydates.ui.navigation.csvExportDestination
import com.kuzepa.mydates.ui.navigation.csvImportDestination
import com.kuzepa.mydates.ui.navigation.dataTransferDestination
import com.kuzepa.mydates.ui.navigation.donationDestination
import com.kuzepa.mydates.ui.navigation.eventCreatorDestination
import com.kuzepa.mydates.ui.navigation.eventEditorDestination
import com.kuzepa.mydates.ui.navigation.eventTypesDestination
import com.kuzepa.mydates.ui.navigation.eventsDestination
import com.kuzepa.mydates.ui.navigation.helpDestination
import com.kuzepa.mydates.ui.navigation.labelCreatorDestination
import com.kuzepa.mydates.ui.navigation.labelEditorDestination
import com.kuzepa.mydates.ui.navigation.labelsDestination
import com.kuzepa.mydates.ui.navigation.moreDestination
import com.kuzepa.mydates.ui.navigation.navigateToAbout
import com.kuzepa.mydates.ui.navigation.navigateToBulkLabelAssignment
import com.kuzepa.mydates.ui.navigation.navigateToContactsImport
import com.kuzepa.mydates.ui.navigation.navigateToCsvExport
import com.kuzepa.mydates.ui.navigation.navigateToCsvImport
import com.kuzepa.mydates.ui.navigation.navigateToDataTransfer
import com.kuzepa.mydates.ui.navigation.navigateToDonation
import com.kuzepa.mydates.ui.navigation.navigateToEventCreator
import com.kuzepa.mydates.ui.navigation.navigateToEventEditor
import com.kuzepa.mydates.ui.navigation.navigateToEventTypeEditor
import com.kuzepa.mydates.ui.navigation.navigateToEventTypes
import com.kuzepa.mydates.ui.navigation.navigateToHelp
import com.kuzepa.mydates.ui.navigation.navigateToLabelEditor
import com.kuzepa.mydates.ui.navigation.navigateToLabels
import com.kuzepa.mydates.ui.navigation.navigateToNotificationFilter
import com.kuzepa.mydates.ui.navigation.navigateToNotificationSettings
import com.kuzepa.mydates.ui.navigation.navigateToNotificationTroubleshoot
import com.kuzepa.mydates.ui.navigation.navigateToSettings
import com.kuzepa.mydates.ui.navigation.navigateToThirdPartyLibraries
import com.kuzepa.mydates.ui.navigation.notificationFilterDestination
import com.kuzepa.mydates.ui.navigation.notificationSettingsDestination
import com.kuzepa.mydates.ui.navigation.notificationTroubleshootDestination
import com.kuzepa.mydates.ui.navigation.searchDestination
import com.kuzepa.mydates.ui.navigation.settingsDestination

@Composable
internal fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    val topLevelDestinations = remember {
        setOf(
            Home,
            Search,
            Appearance,
            More
        )
    }

    val showBottomBar = topLevelDestinations.any { topLevelRoute ->
        currentDestination?.containsRoute(route = topLevelRoute) == true
    }
    val showFab = currentDestination?.containsRoute(route = Home) == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController,
                    currentDestination
                )
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigateToEventCreator() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.event_creator_title)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            eventsDestination(onNavigateToEventEditor = { eventId ->
                navController.navigateToEventEditor(id = eventId)
            })
            eventCreatorDestination()
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
            eventCreatorDestination()
            eventEditorDestination()

            labelsDestination(
                onNavigateToLabelEditor = { labelId ->
                    navController.navigateToLabelEditor(labelId)
                },
                onNavigateToBulkLabelAssignment = { labelId ->
                    navController.navigateToBulkLabelAssignment(labelId)
                })
            labelCreatorDestination()
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
}