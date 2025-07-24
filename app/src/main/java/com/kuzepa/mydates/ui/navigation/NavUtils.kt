package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

internal fun <T : NavRoute> NavDestination?.containsRoute(route: T): Boolean =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true