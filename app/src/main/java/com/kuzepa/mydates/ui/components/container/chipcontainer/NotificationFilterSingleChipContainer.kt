package com.kuzepa.mydates.ui.components.container.chipcontainer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.components.container.BaseNotificationFilterChipContainer
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun NotificationFilterSingleChipContainer(
    containerTitle: String,
    canBeForbidden: Boolean,
    currentState: NotificationFilterState,
    onUpdateState: (NotificationFilterState) -> Unit,
    showHintIcon: Boolean,
    modifier: Modifier = Modifier,
) {
    BaseNotificationFilterChipContainer(
        containerTitle = containerTitle,
        currentState = currentState,
        canBeForbidden = canBeForbidden,
        showStartIcon = false,
        onUpdateState = onUpdateState,
        modifier = modifier,
        startIcon = {},
        showHintIcon = showHintIcon
    )
}

@Preview
@Composable
fun NotificationFilterSingleChipContainerPreview() {
    MyDatesTheme {
        NotificationFilterSingleChipContainer(
            containerTitle = "Notifications",
            canBeForbidden = true,
            currentState = NotificationFilterState.FILTER_STATE_ON,
            onUpdateState = {},
            showHintIcon = true
        )
    }
}