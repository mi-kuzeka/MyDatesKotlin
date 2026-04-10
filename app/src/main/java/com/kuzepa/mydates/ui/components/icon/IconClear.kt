package com.kuzepa.mydates.ui.components.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesColors

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
            painter = painterResource(R.drawable.ic_close),
            contentDescription = stringResource(R.string.clear_button_hint),
            tint = MyDatesColors.placeholderColor,
            modifier = Modifier
                .size(dimensionResource(R.dimen.default_icon_size))
        )
    }
}