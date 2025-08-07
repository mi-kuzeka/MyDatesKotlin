package com.kuzepa.mydates.ui.common.composable.supportingtext

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.kuzepa.mydates.R

@Composable
fun MyDatesSupportingTextBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.padding_default),
                end = dimensionResource(R.dimen.padding_default),
                top = dimensionResource(R.dimen.padding_small),
            )
    ) {
        content()
    }
}