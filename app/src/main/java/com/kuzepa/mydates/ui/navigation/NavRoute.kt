package com.kuzepa.mydates.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
internal abstract class NavRoute

@Serializable
internal abstract class TopLevelRoute : NavRoute()