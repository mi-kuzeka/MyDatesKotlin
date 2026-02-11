package com.kuzepa.mydates.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
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
                iconUnselected = Icons.Outlined.Home,
                iconSelected = Icons.Filled.Home
            ),
            BottomNavItem(
                label = "Search",
                route = Search,
                iconUnselected = Icons.Outlined.Search,
                iconSelected = Icons.Filled.Search
            ),
            BottomNavItem(
                label = "Appearance",
                route = Appearance,
                iconUnselected = Icons.Outlined.Palette,
                iconSelected = Icons.Filled.Palette
            ),
            BottomNavItem(
                label = "More",
                route = More,
                iconUnselected = Icons.Outlined.Menu,
                iconSelected = Icons.Filled.Menu
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
                                if (selected) bottomNavItem.iconSelected else bottomNavItem.iconUnselected,
                                contentDescription = bottomNavItem.label
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
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.event_creator_title)
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
    val iconUnselected: ImageVector,
    val iconSelected: ImageVector
)