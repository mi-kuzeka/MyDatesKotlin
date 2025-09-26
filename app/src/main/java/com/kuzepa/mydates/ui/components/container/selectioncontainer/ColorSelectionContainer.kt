package com.kuzepa.mydates.ui.components.container.selectioncontainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.common.util.labelcolor.toColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.domain.model.label.IconType
import com.kuzepa.mydates.ui.components.chip.CustomColorChip
import com.kuzepa.mydates.ui.components.chip.IconChip
import com.kuzepa.mydates.ui.components.container.BaseContainer
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun ColorSelectionContainer(
    containerTitle: String,
    selectedColorId: Int,
    onSelected: (Int) -> Unit,
    onSelectCustomColor: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val allColors = remember { LabelColor.getAllLabelColors() }
    val gradientColors: List<Color> = remember { LabelColor.getGradientColors() }
    val isCustomColor = remember(key1 = selectedColorId) { LabelColor.isCustomColor(selectedColorId) }

    BaseContainer(
        containerTitle = containerTitle,
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            ),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            ),
        ) {
            allColors.forEach { labelColor ->
                key(labelColor.id) {
                    IconChip(
                        chipSize = dimensionResource(R.dimen.color_chip_size),
                        color = labelColor.color,
                        iconType = IconType.NO_ICON,
                        selectable = true,
                        selected = selectedColorId == labelColor.id,
                        onClick = { onSelected(labelColor.id) }
                    )
                }
            }
            CustomColorChip(
                chipSize = dimensionResource(R.dimen.color_chip_size),
                customColor = if (isCustomColor) selectedColorId.toColor() else null,
                selected = isCustomColor,
                imageVector = Icons.Outlined.Palette,
                iconColor = if (isCustomColor) {
                    selectedColorId.toColor().getContrastedColor()
                } else {
                    Color.White
                },
                gradientColors = gradientColors,
                onClick = {
                    onSelectCustomColor(if (isCustomColor) selectedColorId else null)
                }
            )
        }
    }
}

@Preview
@Composable
fun ColorSelectionContainerPreview() {
    MyDatesTheme {
        ColorSelectionContainer(
            containerTitle = "Tag color",
            selectedColorId = LabelColor.GREEN.id,
            onSelected = {},
            onSelectCustomColor = {}
        )
    }
}

@Preview
@Composable
fun ColorSelectionContainerPreviewCustomColor() {
    MyDatesTheme {
        ColorSelectionContainer(
            containerTitle = "Tag color",
            selectedColorId = Color.Cyan.toInt(),
            onSelected = {},
            onSelectCustomColor = {}
        )
    }
}