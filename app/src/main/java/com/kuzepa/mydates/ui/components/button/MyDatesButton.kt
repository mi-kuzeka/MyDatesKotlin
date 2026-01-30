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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun MyDatesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    text: String? = null,
    isPrimary: Boolean = false
) {
    val textColor = if (isPrimary) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .defaultMinSize(
                minWidth = dimensionResource(R.dimen.min_clickable_size),
                minHeight = dimensionResource(R.dimen.min_clickable_size)
            )
            .clip(Shapes.buttonShape)
            .background(
                if (isPrimary) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            )
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
            iconPainter?.let {
                Icon(
                    painter = iconPainter,
                    contentDescription = "",
                    tint = textColor,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_help_size))
                )
            }
            text?.let {
                Text(
                    text = text,
                    color = textColor,
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
            iconPainter = painterResource(R.drawable.ic_refresh),
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyDatesButtonIconNightPreview() {
    MyDatesTheme {
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_refresh),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun MyDatesButtonIconTextPreview() {
    MyDatesTheme {
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_refresh),
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
            iconPainter = painterResource(R.drawable.ic_refresh),
            text = "Some text",
            onClick = {}
        )
    }
}

@Preview
@Composable
fun MyDatesButtonIconPrimaryTextPreview() {
    MyDatesTheme {
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_refresh),
            text = "Some text",
            onClick = {},
            isPrimary = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyDatesButtonIconTextPrimaryNightPreview() {
    MyDatesTheme {
        MyDatesButton(
            iconPainter = painterResource(R.drawable.ic_refresh),
            text = "Some text",
            onClick = {},
            isPrimary = true
        )
    }
}