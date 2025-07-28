package com.kuzepa.mydates.ui.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kuzepa.mydates.R

@Composable
fun IconClearWithTextFieldValue(
    onValueChange: (TextFieldValue) -> Unit
) {
    CommonIconClear(
        modifier = Modifier
            .clickable {
                onValueChange(
                    TextFieldValue(
                        text = "",
                        selection = TextRange(0)
                    )
                )
            }
    )
}

@Composable
fun IconClear(
    onValueChange: (String) -> Unit
) {
    CommonIconClear(
        modifier = Modifier
            .clickable {
                onValueChange("")
            }
    )
}

@Composable
private fun CommonIconClear(
    modifier: Modifier
) {
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = stringResource(R.string.clear_button_hint),
        tint = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}