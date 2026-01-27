package com.kuzepa.mydates.ui.components.container.selectioncontainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.ui.components.chip.IconChip
import com.kuzepa.mydates.ui.components.container.BaseContainer
import com.kuzepa.mydates.ui.components.dialog.EmojiDialog
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun IconSelectionContainer(
    containerTitle: String,
    selectedIcon: LabelIcon,
    emoji: String,
    firstLetter: String,
    color: Color,
    iconColor: Color,
    onSelected: (LabelIcon) -> Unit,
    showEmojiPicker: Boolean,
    onEmojiPicked: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val allIcons = remember { LabelIcon.getAllIcons() }

    BaseContainer(
        containerTitle = containerTitle,
        modifier = modifier
    ) {
        if (showEmojiPicker) {
            EmojiDialog(
                onCloseDialog = {
                    onEmojiPicked(it)
                }
            )
        }
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
                        emoji = emoji,
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

@Preview
@Composable
fun LabelIconSelectionContainerPreview() {
    MyDatesTheme {
        IconSelectionContainer(
            containerTitle = "Icon",
            selectedIcon = LabelIcon.FAVORITE,
            emoji = "\uD83D\uDE42",
            firstLetter = "f",
            color = LabelColor.GREEN.color,
            iconColor = LabelColor.GREEN.color.getContrastedColor(),
            onSelected = {},
            showEmojiPicker = false,
            onEmojiPicked = {}
        )
    }
}

@Preview
@Composable
fun LabelIconSelectionContainerPreviewExpanded() {
    MyDatesTheme {
        IconSelectionContainer(
            containerTitle = "Icon",
            selectedIcon = LabelIcon.CONSTRUCTION,
            emoji = "\uD83D\uDE42",
            firstLetter = "f",
            color = LabelColor.RED.color,
            iconColor = LabelColor.RED.color.getContrastedColor(),
            onSelected = {},
            showEmojiPicker = false,
            onEmojiPicked = {}
        )
    }
}