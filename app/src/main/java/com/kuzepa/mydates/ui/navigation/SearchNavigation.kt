package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kuzepa.mydates.feature.search.SearchScreen
import com.kuzepa.mydates.ui.components.ScreenWithBottomBar
import kotlinx.serialization.Serializable

@Serializable
internal object Search : TopLevelRoute()

fun NavGraphBuilder.searchDestination(
    navController: NavHostController,) {
    composable<Search> {
        ScreenWithBottomBar(navController) { modifier ->
            SearchScreen(
                modifier = modifier
            )
        }
    }
}