package com.kuzepa.mydates.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.kuzepa.mydates.ui.components.baseeditor.GoBackConfirmationDialog
import com.kuzepa.mydates.ui.theme.MyDatesColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenWithTopBar(
    title: String,
    onNewItemFabClick: () -> Unit,
    newItemFabDescription: String,
    onNavigateBack: () -> Unit,
    showGoBackDialog: Boolean,
    onShowGoBackDialogChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable () -> Unit
) {
    BackHandler {
        if (showGoBackDialog) {
            onShowGoBackDialogChange(true)
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = title,
                canGoBack = true,
                onGoBack = {
                    if (showGoBackDialog) {
                        onShowGoBackDialogChange(true)
                    } else {
                        onNavigateBack()
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = onNewItemFabClick,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = newItemFabDescription
                    )
                }
        },
        // Connects the Scaffold's scroll to the TopAppBar's collapse/expand behavior
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .imePadding(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyDatesColors.screenBackground)
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.exclude(WindowInsets.ime)
                        .only(WindowInsetsSides.Horizontal)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.TopCenter)
            ) {
                content()
            }

            if (showGoBackDialog) {
                GoBackConfirmationDialog(
                    onDismissDialog = { onShowGoBackDialogChange(false) },
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}