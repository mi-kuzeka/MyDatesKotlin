package com.kuzepa.mydates.ui.common.utils.dateformat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
fun rememberDateFormatter(): DateFormatterWrapper {
    val context = LocalContext.current
    return remember {
        EntryPointAccessors.fromApplication(
            context,
            DateFormatterEntryPoint::class.java
        ).dateFormatterWrapper()
    }
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface DateFormatterEntryPoint {
    fun dateFormatterWrapper(): DateFormatterWrapper
}