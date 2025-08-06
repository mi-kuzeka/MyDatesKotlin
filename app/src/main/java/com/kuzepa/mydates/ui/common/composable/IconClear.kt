package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors

@Composable
fun IconClear(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onValueChange("") },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear_button_hint),
            tint = MyDatesColors.placeholderColor
        )
    }
}