package com.kuzepa.mydates.feature.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuzepa.mydates.R
import com.kuzepa.mydates.feature.main.components.BottomNavigationBar
import com.kuzepa.mydates.ui.navigation.Appearance
import com.kuzepa.mydates.ui.navigation.Home
import com.kuzepa.mydates.ui.navigation.More
import com.kuzepa.mydates.ui.navigation.MyDatesNavHost
import com.kuzepa.mydates.ui.navigation.Search
import com.kuzepa.mydates.ui.navigation.containsRoute
import com.kuzepa.mydates.ui.navigation.navigateToEventEditor

@Composable
internal fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    val topLevelDestinations = remember {
        setOf(
            Home,
            Search,
            Appearance,
            More
        )
    }

    val showBottomBar = topLevelDestinations.any { topLevelRoute ->
        currentDestination?.containsRoute(route = topLevelRoute) == true
    }
    val showFab = currentDestination?.containsRoute(route = Home) == true

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController,
                    currentDestination
                )
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
        MyDatesNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Vertical,
                    ),
                ),
        )
    }
}