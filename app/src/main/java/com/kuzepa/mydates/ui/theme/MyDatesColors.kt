package com.kuzepa.mydates.ui.theme

import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
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
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.surface

    val containerColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainer

    val focusedContainerColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainerHighest

    val chipContainerColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.secondaryContainer

    val actionChipContainerColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primaryContainer

    val actionChipTextColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.onPrimaryContainer

    /* Text colors */
    val defaultTextColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.onSurface

    val errorTextColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.error

    val accentTextColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val textFieldLabelColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.secondary

    val focusedTextFieldLabelColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val placeholderColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.outline

    /* Checkbox colors */
    val checkBoxTextColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val checkBoxColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primary

    /* Chip colors */
    val notificationOnChipColors: SelectableChipColors
        @Composable
        get() = InputChipDefaults.inputChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
        )

    val notificationOffChipColors: SelectableChipColors
        @Composable
        get() = InputChipDefaults.inputChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            containerColor = MaterialTheme.colorScheme.outlineVariant,
            leadingIconColor = MaterialTheme.colorScheme.secondary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.secondary,
            labelColor = MaterialTheme.colorScheme.secondary,
            selectedLabelColor = MaterialTheme.colorScheme.secondary,
        )

    val notificationForbiddenChipColors: SelectableChipColors
        @Composable
        get() = InputChipDefaults.inputChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            leadingIconColor = MaterialTheme.colorScheme.onErrorContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer,
            labelColor = MaterialTheme.colorScheme.onErrorContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
        )

    val selectedChipColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val unselectedChipColor: Color
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.secondaryContainer


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
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.colorScheme.secondaryContainer

}