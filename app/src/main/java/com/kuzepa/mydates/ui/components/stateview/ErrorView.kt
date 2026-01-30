package com.kuzepa.mydates.ui.components.stateview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.button.MyDatesButton

@Composable
fun ErrorView(
    onRetry: () -> Unit,
    onContactSupport: () -> Unit,
    errorMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = dimensionResource(R.dimen.padding_default)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(16.dp))
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_refresh),
            text = stringResource(R.string.button_retry),
            onClick = onRetry,
            isPrimary = true,
        )
        Spacer(Modifier.height(16.dp))
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_contact_support),
            text = stringResource(R.string.contact_support),
            onClick = { onContactSupport() },
        )
    }
}