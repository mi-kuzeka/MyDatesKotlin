package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuzepa.mydates.ui.activities.search.composable.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Search : TopLevelRoute()

fun NavGraphBuilder.searchDestination() {
    composable<Search> {
        SearchScreen()
    }
}