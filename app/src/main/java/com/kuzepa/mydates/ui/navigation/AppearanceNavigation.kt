package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kuzepa.mydates.feature.appearance.AppearanceScreen
import com.kuzepa.mydates.ui.components.ScreenWithBottomBar
import kotlinx.serialization.Serializable

@Serializable
internal object Appearance : TopLevelRoute()

fun NavGraphBuilder.appearanceDestination(
    navController: NavHostController,
) {
    composable<Appearance> {
        ScreenWithBottomBar(navController) { modifier ->
            AppearanceScreen(
                modifier = modifier
            )
        }
    }
}