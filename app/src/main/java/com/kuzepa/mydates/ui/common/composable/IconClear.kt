package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kuzepa.mydates.R

@Composable
fun IconClear(onValueChange: (String) -> Unit) {
    IconButton(onClick = { onValueChange("") }) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear_button_hint),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}