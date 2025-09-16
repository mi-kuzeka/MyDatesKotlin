package com.kuzepa.mydates.common.util.labelcolor

import androidx.compose.ui.graphics.Color
import com.kuzepa.mydates.ui.theme.labelBlue6
import com.kuzepa.mydates.ui.theme.labelGreen4
import com.kuzepa.mydates.ui.theme.labelGrey9
import com.kuzepa.mydates.ui.theme.labelLightBlue5
import com.kuzepa.mydates.ui.theme.labelOrange2
import com.kuzepa.mydates.ui.theme.labelPink8
import com.kuzepa.mydates.ui.theme.labelRed1
import com.kuzepa.mydates.ui.theme.labelViolet7
import com.kuzepa.mydates.ui.theme.labelYellow3

enum class LabelColor(val id: Int, val color: Color) {
    RED(1, labelRed1),
    ORANGE(2, labelOrange2),
    YELLOW(3, labelYellow3),
    GREEN(4, labelGreen4),
    LIGHT_BLUE(5, labelLightBlue5),
    BLUE(6, labelBlue6),
    VIOLET(7, labelViolet7),
    PINK(8, labelPink8),
    GREY(9, labelGrey9);

    companion object {
        fun fromId(id: Int) = LabelColor.entries.find { it.id == id }
        fun getColorFromId(id: Int): Color = fromId(id)?.color ?: id.toColor()
        fun isCustomColor(id: Int): Boolean = fromId(id) == null
        fun getAllLabelColors(): List<LabelColor> = LabelColor.entries
        fun getGradientColors(): List<Color> =
            (LabelColor.entries.filter { it != GREY } + RED).map { it.color }

    }
}