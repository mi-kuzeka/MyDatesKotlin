package com.kuzepa.mydates.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kuzepa.mydates.ui.navigation.Appearance
import com.kuzepa.mydates.ui.navigation.Home
import com.kuzepa.mydates.ui.navigation.More
import com.kuzepa.mydates.ui.navigation.Search
import com.kuzepa.mydates.ui.navigation.TopLevelRoute
import com.kuzepa.mydates.ui.navigation.containsRoute

@Composable
internal fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: NavDestination?
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

    NavigationBar {
        bottomNavItems.forEach { bottomNavItem ->
            val selected = currentRoute?.containsRoute(route = bottomNavItem.route) == true
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
}

internal data class BottomNavItem<T : TopLevelRoute>(
    val label: String,
    val route: T,
    val iconUnselected: ImageVector,
    val iconSelected: ImageVector
)