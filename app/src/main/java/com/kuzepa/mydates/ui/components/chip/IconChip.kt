package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.domain.model.label.IconType
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun IconChip(
    chipSize: Dp,
    color: Color,
    iconType: IconType,
    modifier: Modifier = Modifier,
    firstLetter: String = "",
    iconDrawableResId: Int? = null,
    iconColor: Color = Color.Transparent,
    selectable: Boolean = false,
    selected: Boolean = false,
    onSelected: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(chipSize)
            .chipBorder(
                selectable = selectable,
                selected = selected,
                width = 2.dp,
                colorSelected = MyDatesColors.selectedChipColor,
                colorUnselected = MyDatesColors.unselectedChipColor,
                shape = Shapes.labelColorChipShape
            )
            .clip(Shapes.labelColorChipShape)
            .padding(1.dp)
            .background(color)
            .clickable { onSelected() }
    ) {
        when (iconType) {
            IconType.FIRST_LETTER -> {
                BasicText(
                    text = firstLetter,
                    style = MaterialTheme.typography.labelLarge,
                    autoSize = TextAutoSize.StepBased(),
                    color = { iconColor },
                    modifier = Modifier
                        .padding(3.dp)
                )
            }

            IconType.ICON -> {
                iconDrawableResId?.let {
                    Icon(
                        painter = painterResource(it),
                        tint = iconColor,
                        contentDescription = "",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            else -> {}
        }
    }
}

fun Modifier.chipBorder(
    selectable: Boolean,
    selected: Boolean,
    width: Dp = 2.dp,
    colorSelected: Color,
    colorUnselected: Color,
    shape: Shape
): Modifier {
    return if (selectable) {
        border(
            width = width,
            color = if (selected) colorSelected else colorUnselected,
            shape = shape
        )
    } else {
        Modifier
    }
}

@Preview
@Composable
fun SelectableChipPreviewSelected() {
    MyDatesTheme {
        IconChip(
            chipSize = InputChipDefaults.AvatarSize,
            color = LabelColor.LIGHT_BLUE.color,
            iconType = IconType.NO_ICON,
            firstLetter = "",
            iconColor = LabelColor.GREY.color,
            selectable = true,
            selected = true,
        )
    }
}

@Preview
@Composable
fun SelectableChipPreviewUnselected() {
    MyDatesTheme {
        IconChip(
            chipSize = InputChipDefaults.AvatarSize,
            color = LabelColor.LIGHT_BLUE.color,
            iconType = IconType.NO_ICON,
            firstLetter = "",
            iconColor = LabelColor.GREY.color,
            selectable = true,
            selected = false,
        )
    }
}

@Preview
@Composable
fun SelectableChipPreviewFirstLetter() {
    MyDatesTheme {
        IconChip(
            chipSize = 48.dp,
            color = LabelColor.LIGHT_BLUE.color,
            iconType = IconType.FIRST_LETTER,
            firstLetter = "F",
            iconColor = Color.Black,
            selectable = true,
            selected = false,
        )
    }
}

@Preview
@Composable
fun SelectableChipPreviewIcon() {
    MyDatesTheme {
        IconChip(
            chipSize = 48.dp,
            color = LabelColor.LIGHT_BLUE.color,
            iconType = IconType.ICON,
            firstLetter = "F",
            iconDrawableResId = LabelIcon.KEY_ICON.drawableRes,
            iconColor = Color.Black,
            selectable = true,
            selected = false,
        )
    }
}