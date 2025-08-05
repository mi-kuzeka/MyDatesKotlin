package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun getTextFieldColors(): TextFieldColors =
    TextFieldDefaults.colors(
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = getContainerColor(),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer
    )

@Composable
internal fun getPlaceholderColor(): Color = MaterialTheme.colorScheme.outline

@Composable
internal fun getContainerColor(): Color = MaterialTheme.colorScheme.surfaceContainer

@Composable
internal fun getCheckBoxTextColor(): Color = MaterialTheme.colorScheme.primary

@Composable
internal fun getDefaultTextColor(): Color = MaterialTheme.colorScheme.onSurface