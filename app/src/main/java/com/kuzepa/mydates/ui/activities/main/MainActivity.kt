package com.kuzepa.mydates.ui.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kuzepa.mydates.ui.activities.main.composable.MainScreen
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDatesTheme {
                MainScreen()
            }
        }
    }
}