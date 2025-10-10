package com.kuzepa.mydates.feature.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kuzepa.mydates.ui.navigation.MyDatesNavHost
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    MyDatesTheme {
        MyDatesNavHost(navController = navController)
    }
}