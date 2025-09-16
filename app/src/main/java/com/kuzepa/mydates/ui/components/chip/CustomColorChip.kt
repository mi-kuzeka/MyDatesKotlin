package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun CustomColorChip(
    chipSize: Dp,
    customColor: Color?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    iconColor: Color = Color.White,
    onClick: () -> Unit = {},
    gradientColors: List<Color> = emptyList(),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(chipSize)
            .chipBorder(
                selectable = true,
                selected = selected,
                width = 2.dp,
                colorSelected = MyDatesColors.selectedChipColor,
                colorUnselected = MyDatesColors.unselectedChipColor,
                shape = Shapes.labelColorChipShape
            )
            .clip(Shapes.labelColorChipShape)
            .padding(1.dp)
            .gradientBackground(
                customColor = customColor,
                gradientColors = gradientColors
            )
            .clickable { onClick() }
    ) {
        imageVector?.let {
            Icon(
                imageVector = it,
                tint = iconColor,
                contentDescription = "",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

private fun Modifier.gradientBackground(
    customColor: Color?,
    gradientColors: List<Color>
): Modifier {
    return if (customColor == null) {
        background(
            Brush.sweepGradient(gradientColors)
        )
    } else {
        background(customColor)
    }
}

@Preview
@Composable
fun GradientChipPreview() {
    val colors = LabelColor.getGradientColors()
    MyDatesTheme {
        CustomColorChip(
            chipSize = InputChipDefaults.AvatarSize,
            customColor = null,
            selected = false,
            imageVector = Icons.Outlined.Palette,
            gradientColors = colors
        )
    }
}

@Preview
@Composable
fun GradientChipPreviewCustomColor() {
    val colors = LabelColor.getGradientColors()
    MyDatesTheme {
        CustomColorChip(
            chipSize = InputChipDefaults.AvatarSize,
            customColor = Color.Blue,
            selected = true,
            imageVector = Icons.Outlined.Palette,
            gradientColors = colors
        )
    }
}