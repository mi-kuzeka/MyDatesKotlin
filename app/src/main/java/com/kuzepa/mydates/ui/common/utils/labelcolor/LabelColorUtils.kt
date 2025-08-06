package com.kuzepa.mydates.ui.common.utils.labelcolor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.kuzepa.mydates.ui.theme.labelBlue6
import com.kuzepa.mydates.ui.theme.labelGreen4
import com.kuzepa.mydates.ui.theme.labelGrey9
import com.kuzepa.mydates.ui.theme.labelLightBlue5
import com.kuzepa.mydates.ui.theme.labelOrange2
import com.kuzepa.mydates.ui.theme.labelPink8
import com.kuzepa.mydates.ui.theme.labelRed1
import com.kuzepa.mydates.ui.theme.labelViolet7
import com.kuzepa.mydates.ui.theme.labelYellow3
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun Color.toInt(): Int = this.toArgb()

fun Int.toColor() = Color(this)

fun getEventLabelColor(colorCode: Int): Color {
    if (colorCode.isCustomEventLabelColor()) {
        return colorCode.toColor()
    }
    return when (colorCode) {
        1 -> labelRed1
        2 -> labelOrange2
        3 -> labelYellow3
        4 -> labelGreen4
        5 -> labelLightBlue5
        6 -> labelBlue6
        7 -> labelViolet7
        8 -> labelPink8
        9 -> labelGrey9
        else -> Color.White
    }
}

fun Int.isCustomEventLabelColor() = this !in 1..9

fun Color.getContrastedColor(
    lightTarget: Float = 0.85f,
    darkTarget: Float = 0.15f,
    grayscaleFallback: Color = Color.Black
): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    val (hue, saturation, lightness) = hsl

    // Handle grayscale (white/black/gray)
    if (saturation < 0.05f) { // Nearly grayscale
        return if (lightness > 0.5f) grayscaleFallback
        else Color.White
    }

    val targetLightness = if (lightness > 0.5f) {
        max(darkTarget, lightness - 0.4f) // Darken less aggressively
    } else {
        min(lightTarget, lightness + 0.4f) // Lighten less aggressively
    }

    val targetSaturation = when {
        saturation < 0.3f -> 0.7f // Boost low saturation
        saturation > 0.8f -> 0.8f // Prevent over-saturation
        else -> saturation
    }

    return Color.hsl(hue, targetSaturation, targetLightness)
}

val randomColor
    get() = Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
