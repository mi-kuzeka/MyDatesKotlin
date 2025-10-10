package com.kuzepa.mydates.feature.more.colorpicker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorPickerUiState(
    val color: Color = Color.White
)