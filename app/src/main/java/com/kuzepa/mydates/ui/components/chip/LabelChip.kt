package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.domain.formatter.labelicon.IconType
import com.kuzepa.mydates.domain.formatter.labelicon.getIconResIdByCode
import com.kuzepa.mydates.domain.formatter.labelicon.getIconType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.ui.components.icon.IconRemove
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.common.util.labelcolor.getEventLabelColor
import com.kuzepa.mydates.common.util.labelcolor.randomColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import kotlin.random.Random

@Composable
fun LabelChip(
    label: Label,
    onLabelClick: (id: String) -> Unit,
    onRemoveLabelClick: (id: String) -> Unit,
    buttonRemoveDescription: String,
    modifier: Modifier = Modifier
) {
    var firstLetter by remember(label.name) {
        mutableStateOf(label.name.first().toString())
    }
    var labelColor by remember(label.color) {
        mutableStateOf(getEventLabelColor(label.color))
    }
    var labelIconColor by remember(labelColor) {
        mutableStateOf(labelColor.getContrastedColor())
    }
    var iconType by remember(label.iconId) {
        mutableStateOf(getIconType(label.iconId))
    }
    var iconId by remember(label.iconId) {
        mutableStateOf(getIconResIdByCode(label.iconId))
    }
    InputChip(
        label = {
            Text(
                text = label.name,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        selected = true,
        onClick = { onLabelClick(label.id) },
        leadingIcon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(InputChipDefaults.AvatarSize)
                    .clip(Shapes.labelChipShape)
                    .background(color = labelColor)
            ) {
                when (iconType) {
                    IconType.FIRST_LETTER -> {
                        Text(
                            text = firstLetter,
                            style = MaterialTheme.typography.bodyMedium,
                            color = labelIconColor
                        )
                    }

                    IconType.ICON -> {
                        iconId?.let {
                            Icon(
                                painter = painterResource(it),
                                tint = labelIconColor,
                                contentDescription = "",
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    else -> {}
                }
            }
        },
        trailingIcon = {
            IconRemove(
                onClick = { onRemoveLabelClick(label.id) },
                contentDescription = buttonRemoveDescription,
                modifier = Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = MyDatesColors.chipContainerColor,
            containerColor = MyDatesColors.chipContainerColor,
            trailingIconColor = MyDatesColors.placeholderColor,
            selectedTrailingIconColor = MyDatesColors.placeholderColor,
            labelColor = MyDatesColors.defaultTextColor,
            selectedLabelColor = MyDatesColors.defaultTextColor,
        ),
        elevation = InputChipDefaults.inputChipElevation(
            elevation = 2.dp
        ),
        shape = Shapes.labelChipShape,
        modifier = modifier
    )
}

@Preview
@Composable
fun LabelChipPreview() {
    MyDatesTheme {
        LabelChip(
            label = Label(
                id = "1",
                name = "Friends",
                color = randomColor.toInt(),
                notificationState = NotificationFilterState.FILTER_STATE_ON,
                iconId = Random.nextInt(69)
            ),
            onLabelClick = {},
            onRemoveLabelClick = {},
            buttonRemoveDescription = ""
        )
    }
}