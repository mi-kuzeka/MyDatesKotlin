package com.kuzepa.mydates.ui.common.composable.eventlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.dateformat.DateShowingMode
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.common.utils.dateformat.toFormattedDate
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun EventItem(
    event: Event?,
    onNavigateToEventEditor: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    event?.let {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onNavigateToEventEditor(event.id) })
                .padding(dimensionResource(R.dimen.padding_small))
                .height(IntrinsicSize.Min)
        ) {
            if (event.image == null) {
                // TODO show image from resources
            } else {
                Image(bitmap = event.image.asImageBitmap(), contentDescription = null)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MyDatesColors.accentTextColor
                )
                Row {
                    Text(
                        text = event.date.toFormattedDate(DateShowingMode.VIEW_MODE),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MyDatesColors.textFieldLabelColor
                    )
                    //TODO show zodiac icon if active
                }
                Text(
                    text = event.type.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MyDatesColors.textFieldLabelColor
                )
                // TODO labels
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                // TODO show age
                Text(
                    "age",
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
                // TODO show actual info
                Text(
                    "days left",
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview
@Composable
fun EventItemPreview() {
    MyDatesTheme {
        val event = Event(
            id = 1,
            name = "Bob",
            date = EventDate(month = 8, day = 19, year = 2002),
            type = EventType(
                id = "1",
                name = "Birthday",
                isDefault = true,
                notificationState = NotificationFilterState.FILTER_STATE_ON,
                showZodiac = true
            ),
            notes = "",
            image = null,
            labels = listOf(
                Label(
                    id = "1",
                    name = "Friends",
                    color = 1,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 1
                ),
                Label(
                    id = "2",
                    name = "Family",
                    color = 2,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 2
                ),
                Label(
                    id = "3",
                    name = "Other",
                    color = 3,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 3
                )
            )
        )
        EventItem(
            event = event,
            onNavigateToEventEditor = {}
        )
    }
}