package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.logs.LogsScreen
import kotlinx.serialization.Serializable


@Serializable
internal data class Logs(val errorMessage: String) : NavRoute()

fun NavGraphBuilder.logsDestination(
    onNavigateBack: () -> Unit
) {
    dialog<Logs> { backStackEntry ->
        val logs: Logs = backStackEntry.toRoute()
        LogsScreen(onDismiss = { onNavigateBack() }, errorMessage = logs.errorMessage)
    }
}

fun NavController.navigateToLogs(errorMessage: String) {
    navigate(route = Logs(errorMessage = errorMessage))
}
