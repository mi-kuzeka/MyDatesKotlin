package com.kuzepa.mydates.feature.logs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.FullScreenLoading
import com.kuzepa.mydates.ui.components.button.MyDatesButton
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    errorMessage: String
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value.sharingEmailIntentState) {
        when (val currentState = uiState.value.sharingEmailIntentState) {
            is LogsSharingEmailIntentState.Loading -> {

            }

            is LogsSharingEmailIntentState.Success -> {
                context.startActivity(currentState.emailClientIntent)
                viewModel.resetSharingState()
            }

            is LogsSharingEmailIntentState.Error -> {
                Toast.makeText(
                    context,
                    currentState.message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetSharingState()
            }
        }
    }

    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.error_message),
            )
        },
        text = {
            when (val loadingState = uiState.value.loadingState) {
                is LogsLoadingState.Loading -> {
                    FullScreenLoading()
                }

                is LogsLoadingState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = loadingState.message,
                            color = MyDatesColors.errorTextColor,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        MyDatesButton(
                            iconPainter = painterResource(R.drawable.ic_refresh),
                            text = stringResource(R.string.button_retry),
                            onClick = {
                                viewModel.loadLogs()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                is LogsLoadingState.NoLogs -> {
                    MyDatesButton(
                        iconPainter = painterResource(R.drawable.ic_report),
                        text = stringResource(R.string.button_send_report),
                        onClick = {
                            viewModel.sendReportWithNoLogs(loadingState.message, errorMessage)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                is LogsLoadingState.Success -> {
                    MyDatesButton(
                        iconPainter = painterResource(R.drawable.ic_report),
                        text = stringResource(R.string.button_send_report),
                        onClick = {
                            viewModel.shareLogs()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        },
        shape = Shapes.defaultDialogShape,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.button_ok))
            }
        },
        containerColor = MyDatesColors.screenBackground
    )
}