package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

/**
 * Returns whether the current nav destination contains the class of the passed [NavRoute] or not.
 * @param route destination you want to check
 */
internal fun <T : NavRoute> NavDestination?.containsRoute(route: T): Boolean =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true