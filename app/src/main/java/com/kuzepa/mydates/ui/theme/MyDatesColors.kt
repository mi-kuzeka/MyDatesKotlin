package com.kuzepa.mydates.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal object MyDatesColors {
    val textFieldColors: TextFieldColors
        @Composable
        get() = TextFieldDefaults.colors(
            focusedLabelColor = focusedTextFieldLabelColor,
            unfocusedLabelColor = textFieldLabelColor,
            focusedTextColor = defaultTextColor,
            unfocusedTextColor = defaultTextColor,
            focusedContainerColor = focusedContainerColor,
            unfocusedContainerColor = containerColor,
            errorContainerColor = containerColor,
            cursorColor = defaultTextColor,
            errorCursorColor = errorTextColor
        )

    /* Container colors */
    val screenBackground: Color
        @Composable
        get() = MaterialTheme.colorScheme.surface

    val containerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainerLow

    val focusedContainerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainerHigh

    val chipContainerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primaryContainer

    val actionChipContainerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val actionChipTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onPrimary

    /* Text colors */
    val defaultTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurface

    val errorTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.error

    val accentTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val textFieldLabelColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.secondary

    val focusedTextFieldLabelColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val placeholderColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.outline

    /* Checkbox colors */
    val checkBoxTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val checkBoxColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary


    val segmentedButtonColors: SegmentedButtonColors
        @Composable
        get() = SegmentedButtonDefaults.colors(
            activeContainerColor = activeContainerColor,
            inactiveContainerColor = containerColor,
            activeBorderColor = activeContainerColor,
            inactiveBorderColor = containerColor,
            activeContentColor = MaterialTheme.colorScheme.primary,
            inactiveContentColor = MaterialTheme.colorScheme.secondary
        )

    val activeContainerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.secondaryContainer

}