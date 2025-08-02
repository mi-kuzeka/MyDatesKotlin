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
    onNavigateToDonation: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    composable<More> {
        MoreScreen(
            onNavigateToEventTypes = onNavigateToEventTypes,
            onNavigateToLabels = onNavigateToLabels,
            onNavigateToDataTransfer = onNavigateToDataTransfer,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToDonation = onNavigateToDonation,
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

fun NavController.navigateToDonation() {
    navigate(route = Donation)
}

fun NavController.navigateToHelp() {
    navigate(route = Help)
}

fun NavController.navigateToAbout() {
    navigate(route = About)
}


@Serializable
internal object Donation : NavRoute()

fun NavGraphBuilder.donationDestination(onNavigateBack: () -> Unit) {
    dialog<Donation> {
//                DonationScreen()
    }
}

@Serializable
internal object Help : NavRoute()

fun NavGraphBuilder.helpDestination(
    onNavigateToNotificationTroubleshoot: () -> Unit,
    onNavigateBack: () -> Unit
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

fun NavGraphBuilder.notificationTroubleshootDestination(onNavigateBack: () -> Unit) {
    dialog<NotificationTroubleshoot> {
        // NotificationTroubleshoot()
    }
}


@Serializable
internal object About : NavRoute()

fun NavGraphBuilder.aboutDestination(
    onNavigateToThirdPartyLibraries: () -> Unit,
    onNavigateBack: () -> Unit
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