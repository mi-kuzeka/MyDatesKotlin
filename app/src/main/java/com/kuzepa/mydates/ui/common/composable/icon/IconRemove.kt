package com.kuzepa.mydates.ui.common.composable.icon

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun IconRemove(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = contentDescription,
            tint = MyDatesColors.placeholderColor
        )
    }
}

@Preview
@Composable
fun IconRemovePreview() {
    MyDatesTheme {
        IconRemove(onClick = {}, contentDescription = "")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun IconRemovePreviewDark() {
    MyDatesTheme {
        IconRemove(onClick = {}, contentDescription = "")
    }
}