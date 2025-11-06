package com.kuzepa.mydates.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.kuzepa.mydates.R

@Composable
internal fun EmptyEventImage(contentDescription: String?, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.empty_list_white),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}