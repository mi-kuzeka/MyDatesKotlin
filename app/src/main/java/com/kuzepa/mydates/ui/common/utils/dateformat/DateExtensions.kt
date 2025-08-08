package com.kuzepa.mydates.ui.common.utils.dateformat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.EventDate

@Composable
fun EventDate.toFormattedDate(showingMode: DateShowingMode): String {
    val dateFormatter = rememberDateFormatter()
    var formattedDate by remember { mutableStateOf("") }

    LaunchedEffect(this) {
        formattedDate =
            dateFormatter.format(
                eventDate = this@toFormattedDate,
                showingMode = showingMode
            )
    }

    return formattedDate
}