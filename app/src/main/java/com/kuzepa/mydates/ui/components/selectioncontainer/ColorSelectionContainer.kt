package com.kuzepa.mydates.ui.components.selectioncontainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun ColorSelectionContainer(
    containerLabel: String,
    selectedColorId: Int,
    onSelected: (Int) -> Unit,
    onSelectCustomColor: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val allColors = remember { LabelColor.getAllLabelColors() }
    val gradientColors: List<Color> = remember { LabelColor.getGradientColors() }
    val isCustomColor = remember(key1 = selectedColorId) { LabelColor.isCustomColor(selectedColorId) }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.defaultContainerShape)
                .background(color = MyDatesColors.containerColor)
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_default),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(
                text = containerLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MyDatesColors.textFieldLabelColor
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_small)),
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
                            onSelected = { onSelected(labelColor.id) }
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
        // Necessary to fill the remaining space to match all container styles
        MyDatesSupportingTextBox {

        }
    }
}

@Preview
@Composable
fun ColorSelectionContainerPreview() {
    MyDatesTheme {
        ColorSelectionContainer(
            containerLabel = "Tag color",
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
            containerLabel = "Tag color",
            selectedColorId = Color.Cyan.toInt(),
            onSelected = {},
            onSelectCustomColor = {}
        )
    }
}