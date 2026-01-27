package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.common.util.labelcolor.randomColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.domain.model.label.IconType
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.components.icon.IconRemove
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes
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
        mutableStateOf(label.name.take(1))
    }
    var labelColor by remember(label.color) {
        mutableStateOf(LabelColor.getColorFromId(label.color))
    }
    var labelIconColor by remember(labelColor) {
        mutableStateOf(labelColor.getContrastedColor())
    }
    var labelIcon by remember(label.iconId) {
        mutableStateOf(LabelIcon.fromId(label.iconId))
    }
    val notificationStateIcon =
        if (label.notificationState == NotificationFilterState.FILTER_STATE_ON) {
            painterResource(R.drawable.ic_notifications_active_filled)
        } else {
            painterResource(R.drawable.ic_notifications_off_filled)
        }
    val notificationStateColor: Color = when (label.notificationState) {
        NotificationFilterState.FILTER_STATE_ON -> MyDatesColors.notificationOnTextColor.copy(alpha = 0.5f)
        NotificationFilterState.FILTER_STATE_OFF -> MyDatesColors.notificationOffTextColor.copy(alpha = 0.5f)
        NotificationFilterState.FILTER_STATE_FORBIDDEN -> MyDatesColors.notificationForbiddenTextColor.copy(alpha = 0.5f)
    }
    InputChip(
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    4.dp
                )
            ) {
                Text(
                    text = label.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    painter = notificationStateIcon,
                    contentDescription = "",
                    tint = notificationStateColor,
                    modifier = Modifier.size(
                        dimensionResource(R.dimen.icon_size)
                    )
                )
            }
        },
        selected = true,
        onClick = { onLabelClick(label.id) },
        leadingIcon = {
            IconChip(
                chipSize = InputChipDefaults.AvatarSize,
                color = labelColor,
                iconType = labelIcon?.iconType ?: IconType.FIRST_LETTER,
                emoji = label.emoji,
                firstLetter = firstLetter,
                iconDrawableResId = labelIcon?.drawableRes,
                iconColor = labelIconColor,
                onClick = { onLabelClick(label.id) }
            )
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
        shape = Shapes.labelChipShape,
        elevation = InputChipDefaults.inputChipElevation(
            elevation = 2.dp
        ),
        modifier = modifier
            .height(dimensionResource(R.dimen.color_chip_size))
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
                iconId = Random.nextInt(69),
                emoji = ""
            ),
            onLabelClick = {},
            onRemoveLabelClick = {},
            buttonRemoveDescription = ""
        )
    }
}