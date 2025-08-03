package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun getMyDatesTextFieldColors(): TextFieldColors =
    TextFieldDefaults.colors(
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedContainerColor = getMyDatesUnfocusedContainerColor(),
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        errorContainerColor = MaterialTheme.colorScheme.errorContainer
    )

@Composable
internal fun getMyDatesPlaceholderColor(): Color = MaterialTheme.colorScheme.outline

@Composable
internal fun getMyDatesUnfocusedContainerColor(): Color = MaterialTheme.colorScheme.surfaceContainer