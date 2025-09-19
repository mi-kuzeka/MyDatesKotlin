package com.kuzepa.mydates.ui.components.container

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.ui.components.chip.NotificationFilterChip
import com.kuzepa.mydates.ui.components.dialog.NotificationFilterHintAlertDialog
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun BaseNotificationFilterChipContainer(
    containerTitle: String,
    currentState: NotificationFilterState,
    canBeForbidden: Boolean,
    showStartIcon: Boolean,
    onUpdateState: (NotificationFilterState) -> Unit,
    modifier: Modifier = Modifier,
    startIcon: @Composable () -> Unit = {},
    showHintIcon: Boolean = false
) {
    var showHintDialog by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        BaseContainer(
            containerTitle = containerTitle
        ) {
            NotificationFilterChip(
                currentState = currentState,
                clickable = true,
                canBeForbidden = canBeForbidden,
                showStartIcon = showStartIcon,
                onUpdateState = onUpdateState,
                startIcon = startIcon
            )
        }
        if (showHintIcon) {
            NotificationHintIcon(
                onClick = { showHintDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
        if (showHintDialog) {
            NotificationFilterHintAlertDialog(
                onCloseDialog = { showHintDialog = false }
            )
        }
    }
}

@Composable
private fun NotificationHintIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.AutoMirrored.Default.HelpOutline,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = stringResource(R.string.notification_filter_help_hint),
        modifier = modifier
            .clickable(onClick = onClick)
            .size(dimensionResource(R.dimen.icon_help_size))
    )
}

@Preview
@Composable
fun BaseNotificationFilterChipContainerPreview() {
    MyDatesTheme {
        BaseNotificationFilterChipContainer(
            containerTitle = "Notifications",
            currentState = NotificationFilterState.FILTER_STATE_ON,
            canBeForbidden = true,
            onUpdateState = {},
            showStartIcon = false,
            startIcon = {},
            showHintIcon = true
        )
    }
}

@Preview
@Composable
fun NotificationHintIconPreview() {
    MyDatesTheme {
        NotificationHintIcon(
            onClick = {}
        )
    }
}