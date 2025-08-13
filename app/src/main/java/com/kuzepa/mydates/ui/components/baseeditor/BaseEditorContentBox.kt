package com.kuzepa.mydates.ui.components.baseeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.kuzepa.mydates.R

@Composable
fun BaseEditorContentBox(
    modifier: Modifier = Modifier,
    addSpacerForFabButton: Boolean = false,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_small),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentSize()
            .verticalScroll(state = scrollState)
            .padding(dimensionResource(R.dimen.padding_default))
    ) {
        content()
        if (addSpacerForFabButton) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.fab_size)))
        }
    }
}