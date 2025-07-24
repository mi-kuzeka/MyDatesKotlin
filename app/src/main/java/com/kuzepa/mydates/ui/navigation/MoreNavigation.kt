package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.kuzepa.mydates.ui.activities.more.composable.MoreScreen
import kotlinx.serialization.Serializable

@Serializable
internal object More : TopLevelRoute()

fun NavGraphBuilder.moreDestination(
    onNavigateToEventTypes: () -> Unit,
    onNavigateToLabels: () -> Unit,
    onNavigateToDataTransfer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSupportProject: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    composable<More> {
        MoreScreen(
            onNavigateToEventTypes = onNavigateToEventTypes,
            onNavigateToLabels = onNavigateToLabels,
            onNavigateToDataTransfer = onNavigateToDataTransfer,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToSupportProject = onNavigateToSupportProject,
            onNavigateToHelp = onNavigateToHelp,
            onNavigateToAbout = onNavigateToAbout
        )
    }
}

fun NavController.navigateToEventTypes() {
    navigate(route = EventTypes)
}

fun NavController.navigateToLabels() {
    navigate(route = Labels)
}

fun NavController.navigateToDataTransfer() {
    navigate(route = DataTransfer)
}

fun NavController.navigateToSettings() {
    navigate(route = Settings)
}

fun NavController.navigateToSupportProject() {
    navigate(route = SupportProject)
}

fun NavController.navigateToHelp() {
    navigate(route = Help)
}

fun NavController.navigateToAbout() {
    navigate(route = About)
}


@Serializable
internal object SupportProject : NavRoute()

fun NavGraphBuilder.supportProjectDestination() {
    dialog<SupportProject> {
//                SupportScreen()
    }
}

@Serializable
internal object Help : NavRoute()

fun NavGraphBuilder.helpDestination(
    onNavigateToNotificationTroubleshoot: () -> Unit
) {
    composable<Help> {
//                HelpScreen(onNavigateToNotificationTroubleshoot = onNavigateToNotificationTroubleshoot)
    }
}

fun NavController.navigateToNotificationTroubleshoot() {
    navigate(route = NotificationTroubleshoot)
}

@Serializable
internal object NotificationTroubleshoot : NavRoute()

fun NavGraphBuilder.notificationTroubleshootDestination() {
    dialog<NotificationTroubleshoot> {
        // NotificationTroubleshoot()
    }
}


@Serializable
internal object About : NavRoute()

fun NavGraphBuilder.aboutDestination(
    onNavigateToThirdPartyLibraries: () -> Unit
) {
    composable<About> {
        // Consider using a dialog, but think about third-party libraries screen
//                AboutScreen(onNavigateToThirdPartyLibraries = onNavigateToThirdPartyLibraries)
    }
}

fun NavController.navigateToThirdPartyLibraries() {
    navigate(route = ThirdPartyLibraries)
}

@Serializable
internal object ThirdPartyLibraries : NavRoute()

fun NavGraphBuilder.thirdPartyLibrariesDestination() {
    dialog<ThirdPartyLibraries> {
//                ThirdPartyLibrariesScreen()
    }
}