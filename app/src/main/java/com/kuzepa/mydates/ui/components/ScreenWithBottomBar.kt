package com.kuzepa.mydates.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.navigation.Appearance
import com.kuzepa.mydates.ui.navigation.Home
import com.kuzepa.mydates.ui.navigation.More
import com.kuzepa.mydates.ui.navigation.Search
import com.kuzepa.mydates.ui.navigation.TopLevelRoute
import com.kuzepa.mydates.ui.navigation.containsRoute
import com.kuzepa.mydates.ui.navigation.navigateToEventEditor

@Composable
internal fun ScreenWithBottomBar(
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                label = "Home",
                route = Home,
                iconUnselectedResId = R.drawable.ic_home,
                iconSelectedResId = R.drawable.ic_home_filled
            ),
            BottomNavItem(
                label = "Search",
                route = Search,
                iconUnselectedResId = R.drawable.ic_search,
                iconSelectedResId = R.drawable.ic_search_filled
            ),
            BottomNavItem(
                label = "Appearance",
                route = Appearance,
                iconUnselectedResId = R.drawable.ic_palette,
                iconSelectedResId = R.drawable.ic_palette_filled
            ),
            BottomNavItem(
                label = "More",
                route = More,
                iconUnselectedResId = R.drawable.ic_menu,
                iconSelectedResId = R.drawable.ic_menu_filled
            )
        )
    }
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    val showFab = currentDestination?.containsRoute(route = Home) == true

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                bottomNavItems.forEach { bottomNavItem ->
                    val selected =
                        currentDestination?.containsRoute(route = bottomNavItem.route) == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(
                                    if (selected) bottomNavItem.iconSelectedResId else bottomNavItem.iconUnselectedResId
                                ),
                                contentDescription = bottomNavItem.label,
                                modifier = Modifier.size(dimensionResource(R.dimen.default_icon_size))
                            )
                        },
                        label = { Text(bottomNavItem.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(bottomNavItem.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigateToEventEditor(id = null) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.event_creator_title),
                        modifier = Modifier.size(dimensionResource(R.dimen.default_icon_size))
                    )
                }
            }
        }
    ) { innerPadding ->
        val stableModifier = remember(innerPadding) {
            Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        }
        content(stableModifier)
    }
}

internal data class BottomNavItem<T : TopLevelRoute>(
    val label: String,
    val route: T,
    @param:DrawableRes val iconUnselectedResId: Int,
    @param:DrawableRes val iconSelectedResId: Int
)