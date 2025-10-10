package com.kuzepa.mydates.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    canGoBack: Boolean,
    onGoBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    endIcon: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (canGoBack) {
                IconButton(
                    onClick = { onGoBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.button_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = { endIcon() },
        title = {
            Text(text = title)
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.exclude(WindowInsets.ime)
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewTopBar() {
    MyDatesTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        TopAppBar(
            title = "Some Screen",
            canGoBack = true,
            onGoBack = { },
            scrollBehavior = scrollBehavior,
            endIcon = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete the item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewTopBarDark() {
    MyDatesTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        TopAppBar(
            title = "Some Screen with long-long-long title",
            canGoBack = true,
            onGoBack = { },
            scrollBehavior = scrollBehavior,
            endIcon = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete the item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}