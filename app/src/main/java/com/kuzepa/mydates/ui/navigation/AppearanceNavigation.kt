package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuzepa.mydates.ui.activities.appearance.composable.AppearanceScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Appearance : TopLevelRoute()

fun NavGraphBuilder.appearanceDestination() {
    composable<Appearance> {
        AppearanceScreen()
    }
}