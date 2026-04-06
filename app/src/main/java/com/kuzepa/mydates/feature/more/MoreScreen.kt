package com.kuzepa.mydates.feature.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.button.MyDatesButton
import com.kuzepa.mydates.ui.theme.MyDatesColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreScreen(
    onNavigateToEventTypes: () -> Unit,
    onNavigateToLabels: () -> Unit,
    onNavigateToDataTransfer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDonation: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MyDatesColors.screenBackground)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .nestedScroll(rememberNestedScrollInteropConnection())
            .padding(all = 8.dp)
    ) {
        // Event types
        MyDatesButton(
            onClick = { onNavigateToEventTypes() },
            text = stringResource(R.string.event_types_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // Labels
        MyDatesButton(
            onClick = { onNavigateToLabels() },
            text = stringResource(R.string.labels_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // Data transfer (import/export)
        MyDatesButton(
            onClick = { onNavigateToDataTransfer() },
            text = stringResource(R.string.data_transfer_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // Settings
        MyDatesButton(
            onClick = { onNavigateToSettings() },
            text = stringResource(R.string.settings_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // Settings
        MyDatesButton(
            onClick = { onNavigateToDonation() },
            text = stringResource(R.string.support_project_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // Help
        MyDatesButton(
            onClick = { onNavigateToHelp() },
            text = stringResource(R.string.help_title),
            modifier = Modifier.fillMaxWidth(),
        )
        // About
        MyDatesButton(
            onClick = { onNavigateToAbout() },
            text = stringResource(R.string.about_title),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}