package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.domain.model.label.IconType
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun NotificationFilterChip(
    currentState: NotificationFilterState,
    clickable: Boolean,
    canBeForbidden: Boolean,
    showStartIcon: Boolean,
    onUpdateState: (NotificationFilterState) -> Unit,
    modifier: Modifier = Modifier,
    startIcon: @Composable () -> Unit = {},
) {
    val chipIcon = when (currentState) {
        NotificationFilterState.FILTER_STATE_ON -> Icons.Outlined.NotificationsActive
        else -> Icons.Outlined.NotificationsOff
    }
    val chipColors = when (currentState) {
        NotificationFilterState.FILTER_STATE_ON -> MyDatesColors.notificationOnChipColors
        NotificationFilterState.FILTER_STATE_OFF -> MyDatesColors.notificationOffChipColors
        NotificationFilterState.FILTER_STATE_FORBIDDEN -> MyDatesColors.notificationForbiddenChipColors
    }
    val chipText = when (currentState) {
        NotificationFilterState.FILTER_STATE_ON -> stringResource(R.string.notification_status_turned_on_hint)
        NotificationFilterState.FILTER_STATE_OFF -> stringResource(R.string.notification_status_turned_off_hint)
        NotificationFilterState.FILTER_STATE_FORBIDDEN -> stringResource(R.string.notification_status_forbidden_hint)
    }
    InputChip(
        label = {
            Text(
                text = chipText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        selected = true,
        onClick = {
            if (clickable) {
                onUpdateState(
                    NotificationFilterState.getNextState(
                        currentState = currentState,
                        canBeForbidden = canBeForbidden
                    )
                )
            }
        },
        leadingIcon = {
            if (showStartIcon) {
                startIcon()
            }
        },
        trailingIcon = {
            Icon(
                imageVector = chipIcon,
                contentDescription = "",
                modifier = Modifier.size(
                    dimensionResource(R.dimen.icon_size)
                )
            )
        },
        colors = chipColors,
        elevation = InputChipDefaults.inputChipElevation(
            elevation = 2.dp
        ),
        shape = Shapes.labelChipShape,
        modifier = modifier
            .height(dimensionResource(R.dimen.color_chip_size))
    )
}

@Preview
@Composable
fun NotificationFilterChipPreviewOn() {
    MyDatesTheme {
        NotificationFilterChip(
            currentState = NotificationFilterState.FILTER_STATE_ON,
            clickable = false,
            canBeForbidden = false,
            showStartIcon = false,
            onUpdateState = {},
        )
    }
}

@Preview
@Composable
fun NotificationFilterChipPreviewOff() {
    MyDatesTheme {
        NotificationFilterChip(
            currentState = NotificationFilterState.FILTER_STATE_OFF,
            clickable = false,
            canBeForbidden = false,
            showStartIcon = true,
            onUpdateState = {},
            startIcon = {
                IconChip(
                    chipSize = InputChipDefaults.AvatarSize,
                    color = LabelColor.GREEN.color,
                    iconType = IconType.ICON,
                    firstLetter = "F",
                    iconDrawableResId = LabelIcon.COFFEE.drawableRes,
                    iconColor = LabelColor.GREEN.color.getContrastedColor()
                )
            }
        )
    }
}

@Preview
@Composable
fun NotificationFilterChipPreviewClickableForbidden() {
    MyDatesTheme {
        NotificationFilterChip(
            currentState = NotificationFilterState.FILTER_STATE_FORBIDDEN,
            clickable = true,
            canBeForbidden = true,
            showStartIcon = false,
            onUpdateState = {},
        )
    }
}