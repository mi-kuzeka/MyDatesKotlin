package com.kuzepa.mydates.ui.components.button

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun MyDatesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String? = null,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .defaultMinSize(
                minWidth = dimensionResource(R.dimen.min_clickable_size),
                minHeight = dimensionResource(R.dimen.min_clickable_size)
            )
            .clip(Shapes.buttonShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(
                onClick = onClick
            )
            .padding(dimensionResource(R.dimen.padding_default))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.padding_small),
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_help_size))
                )
            }
            text?.let {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun MyDatesButtonIconPreview() {
    MyDatesTheme {
        MyDatesButton(
            icon = Icons.Default.Update,
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyDatesButtonIconNightPreview() {
    MyDatesTheme {
        MyDatesButton(
            icon = Icons.Default.Update,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun MyDatesButtonIconTextPreview() {
    MyDatesTheme {
        MyDatesButton(
            icon = Icons.Default.Update,
            text = "Some text",
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyDatesButtonIconTextNightPreview() {
    MyDatesTheme {
        MyDatesButton(
            icon = Icons.Default.Update,
            text = "Some text",
            onClick = {}
        )
    }
}