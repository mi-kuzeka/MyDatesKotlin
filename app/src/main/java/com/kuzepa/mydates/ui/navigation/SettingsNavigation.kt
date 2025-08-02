package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import kotlinx.serialization.Serializable

@Serializable
internal object Settings : NavRoute()

fun NavGraphBuilder.settingsDestination(
    onNavigateToNotificationSettings: () -> Unit,
    onNavigateToNotificationFilter: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Settings> {
//        SettingsScreen(
        //        onNavigateToNotificationSettings = onNavigateToNotificationSettings,
        //        onNavigateToNotificationFilter = onNavigateToNotificationFilter
        //        )
    }
}

fun NavController.navigateToNotificationSettings() {
    navigate(route = NotificationSettings)
}

fun NavController.navigateToNotificationFilter() {
    navigate(route = NotificationFilter)
}

@Serializable
internal object NotificationSettings : NavRoute()

fun NavGraphBuilder.notificationSettingsDestination(onNavigateBack: () -> Unit) {
    dialog<NotificationSettings> {
//                NotificationSettingsScreen()
    }
}


@Serializable
internal object NotificationFilter : NavRoute()

fun NavGraphBuilder.notificationFilterDestination(onNavigateBack: () -> Unit) {
    dialog<NotificationFilter> {
//                NotificationFilterScreen()
    }
}