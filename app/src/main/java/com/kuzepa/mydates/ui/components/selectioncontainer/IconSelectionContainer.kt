package com.kuzepa.mydates.ui.components.selectioncontainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.ui.components.chip.IconChip
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun IconSelectionContainer(
    containerLabel: String,
    selectedIcon: LabelIcon?,
    firstLetter: String,
    color: Color,
    iconColor: Color,
    onSelected: (LabelIcon) -> Unit,
    modifier: Modifier = Modifier
) {
    val allIcons = remember { LabelIcon.getAllIcons() }

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
                allIcons.forEach { labelIcon ->
                    key(labelIcon.id) {
                        IconChip(
                            chipSize = dimensionResource(R.dimen.color_chip_size),
                            color = color,
                            iconType = labelIcon.iconType,
                            iconDrawableResId = labelIcon.drawableRes,
                            iconColor = iconColor,
                            firstLetter = firstLetter,
                            selectable = true,
                            selected = selectedIcon == labelIcon,
                            onSelected = { onSelected(labelIcon) }
                        )
                    }
                }
            }
        }
        // Necessary to fill the remaining space to match all container styles
        MyDatesSupportingTextBox {

        }
    }
}

@Preview
@Composable
fun LabelIconSelectionContainerPreview() {
    MyDatesTheme {
        IconSelectionContainer(
            containerLabel = "Icon",
            selectedIcon = LabelIcon.CONSTRUCTION,
            firstLetter = "f",
            color = LabelColor.GREEN.color,
            iconColor = LabelColor.GREEN.color.getContrastedColor(),
            onSelected = {}
        )
    }
}