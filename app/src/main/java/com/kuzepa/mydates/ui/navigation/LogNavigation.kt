package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.log.LogScreen
import kotlinx.serialization.Serializable


@Serializable
internal data class Log(val errorMessage: String) : NavRoute()

fun NavGraphBuilder.logDestination(
    onNavigateBack: () -> Unit
) {
    dialog<Log> { backStackEntry ->
        val log: Log = backStackEntry.toRoute()
        LogScreen(onDismiss = { onNavigateBack() }, errorMessage = log.errorMessage)
    }
}

fun NavController.navigateToLog(errorMessage: String) {
    navigate(route = Log(errorMessage = errorMessage))
}
