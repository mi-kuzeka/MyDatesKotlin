package com.kuzepa.mydates.ui.components.icon

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun IconDelete(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .size(dimensionResource(R.dimen.default_icon_size))
        )
    }
}

@Preview
@Composable
fun IconDeletePreview() {
    MyDatesTheme {
        IconDelete(onClick = {}, contentDescription = "")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun IconDeletePreviewDark() {
    MyDatesTheme {
        IconDelete(onClick = {}, contentDescription = "")
    }
}