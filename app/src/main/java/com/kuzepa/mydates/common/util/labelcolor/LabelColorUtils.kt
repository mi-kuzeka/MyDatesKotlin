package com.kuzepa.mydates.common.util.labelcolor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun Color.toInt(): Int = this.toArgb()

fun Int.toColor() = Color(this)

fun Color.getContrastedColor(
    grayscaleFallback: Color = Color.Black
): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    val (hue, saturation, lightness) = hsl

    // Handle grayscale (white/black/gray)
    if (saturation < 0.05f) { // Nearly grayscale
        return if (lightness > 0.45f) grayscaleFallback
        else Color.White
    }

    val targetLightness = if (this.green > 0.85f || lightness > 0.62f) {
        max(lightness - 0.6f, 0f)
    } else {
        min(lightness + 0.7f, 1f)
    }

    val targetSaturation = max(saturation, 0.35f)

    return Color.hsl(hue, targetSaturation, targetLightness)
}

val randomColor
    get() = Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
