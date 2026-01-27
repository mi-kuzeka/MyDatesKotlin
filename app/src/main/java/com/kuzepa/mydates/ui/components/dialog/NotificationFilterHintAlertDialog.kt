package com.kuzepa.mydates.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.components.chip.NotificationFilterChip
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun NotificationFilterHintAlertDialog(
    onCloseDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.notification_filter_help_hint)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                NotificationFilterChip(
                    currentState = NotificationFilterState.FILTER_STATE_ON,
                    clickable = false,
                    canBeForbidden = false,
                    showStartIcon = false,
                    onUpdateState = {},
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                )
                Text(
                    text = stringResource(R.string.notification_filter_on_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                )
                NotificationFilterChip(
                    currentState = NotificationFilterState.FILTER_STATE_OFF,
                    clickable = false,
                    canBeForbidden = false,
                    showStartIcon = false,
                    onUpdateState = {},
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_default))
                )
                Text(
                    text = stringResource(R.string.notification_filter_off_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                )
                NotificationFilterChip(
                    currentState = NotificationFilterState.FILTER_STATE_FORBIDDEN,
                    clickable = false,
                    canBeForbidden = true,
                    showStartIcon = false,
                    onUpdateState = {},
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_default))
                )
                Text(
                    text = stringResource(R.string.notification_filter_forbidden_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                )
            }
        },
        shape = Shapes.defaultDialogShape,
        onDismissRequest = { onCloseDialog() },
        confirmButton = {
            TextButton(
                onClick = {
                    onCloseDialog()
                }
            ) {
                Text(stringResource(R.string.button_ok))
            }
        },
        containerColor = MyDatesColors.screenBackground,
        modifier = modifier
    )
}

@Preview
@Composable
fun NotificationFilterHintAlertDialogPreview() {
    MyDatesTheme {
        NotificationFilterHintAlertDialog(
            onCloseDialog = {}
        )
    }
}