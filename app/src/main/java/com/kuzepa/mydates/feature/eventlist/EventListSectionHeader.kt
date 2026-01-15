package com.kuzepa.mydates.feature.eventlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun EventListSectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Preview
@Composable
fun EventListSectionHeaderPreview() {
    MyDatesTheme {
        EventListSectionHeader(
            text = "Today"
        )
    }
}