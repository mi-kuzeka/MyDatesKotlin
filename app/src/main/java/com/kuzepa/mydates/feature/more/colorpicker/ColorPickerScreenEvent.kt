package com.kuzepa.mydates.feature.more.colorpicker

import androidx.compose.ui.graphics.Color

sealed class ColorPickerScreenEvent {
    data class ColorChanged(val color: Color) : ColorPickerScreenEvent()
    object SetDialogResult : ColorPickerScreenEvent()
}