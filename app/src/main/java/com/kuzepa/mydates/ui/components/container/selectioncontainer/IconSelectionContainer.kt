package com.kuzepa.mydates.ui.components.container.selectioncontainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.ui.components.chip.IconChip
import com.kuzepa.mydates.ui.components.container.BaseContainer
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun IconSelectionContainer(
    containerTitle: String,
    selectedIcon: LabelIcon,
    firstLetter: String,
    color: Color,
    iconColor: Color,
    onSelected: (LabelIcon) -> Unit,
    moreIconsTitle: String,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val allIcons = remember { LabelIcon.getAllIcons() }
    val iconExpand = if (isExpanded) {
        Icons.Outlined.KeyboardArrowUp
    } else {
        Icons.Outlined.KeyboardArrowDown
    }

    BaseContainer(
        containerTitle = containerTitle,
        modifier = modifier
    ) {
        IconChip(
            chipSize = dimensionResource(R.dimen.color_chip_size),
            color = color,
            iconType = selectedIcon.iconType,
            iconDrawableResId = selectedIcon.drawableRes,
            iconColor = iconColor,
            firstLetter = firstLetter,
            selectable = false
        )
        Button(
            onClick = {
                onExpandedChanged(!isExpanded)
            },
            shape = Shapes.labelChipShape,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = moreIconsTitle,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small)
                        )
                )
                Icon(
                    imageVector = iconExpand,
                    contentDescription = ""
                )
            }
        }
        if (isExpanded) {
            val chipSize = dimensionResource(R.dimen.color_chip_size)
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
                allIcons.forEach { labelIcon ->
                    key(labelIcon.id) {
                        IconChip(
                            chipSize = chipSize,
                            color = color,
                            iconType = labelIcon.iconType,
                            iconDrawableResId = labelIcon.drawableRes,
                            iconColor = iconColor,
                            firstLetter = firstLetter,
                            selectable = true,
                            selected = selectedIcon == labelIcon,
                            onClick = { onSelected(labelIcon) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LabelIconSelectionContainerPreview() {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    MyDatesTheme {
        IconSelectionContainer(
            containerTitle = "Icon",
            selectedIcon = LabelIcon.FAVORITE,
            firstLetter = "f",
            color = LabelColor.GREEN.color,
            iconColor = LabelColor.GREEN.color.getContrastedColor(),
            onSelected = {},
            moreIconsTitle = "Other icons",
            isExpanded = isExpanded,
            onExpandedChanged = { isExpanded = it }
        )
    }
}

@Preview
@Composable
fun LabelIconSelectionContainerPreviewExpanded() {
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    MyDatesTheme {
        IconSelectionContainer(
            containerTitle = "Icon",
            selectedIcon = LabelIcon.CONSTRUCTION,
            firstLetter = "f",
            color = LabelColor.RED.color,
            iconColor = LabelColor.RED.color.getContrastedColor(),
            onSelected = {},
            moreIconsTitle = "Other icons",
            isExpanded = isExpanded,
            onExpandedChanged = { isExpanded = it }
        )
    }
}