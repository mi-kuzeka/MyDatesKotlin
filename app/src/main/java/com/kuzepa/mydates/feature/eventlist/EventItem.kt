package com.kuzepa.mydates.feature.eventlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.feature.home.components.EmptyEventImage
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun EventItem(
    eventItemData: EventItemData?,
    onNavigateToEventEditor: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    eventItemData?.let { data ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.padding_extra_small)
            ),
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onNavigateToEventEditor(eventItemData.event.id) })
                .padding(dimensionResource(R.dimen.padding_small))
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = modifier
                    .size(
                        dimensionResource(R.dimen.event_item_image_box_size)
                    )
                    .clip(shape = Shapes.smallImageBoxShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (data.event.image == null) {
                    EmptyEventImage(
                        contentDescription = null,
                        modifier = Modifier
                            .padding(all = dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Image(bitmap = data.event.image.asImageBitmap(), contentDescription = null)
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = data.event.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MyDatesColors.accentTextColor
                )
                Row {
                    Text(
                        text = data.formattedDate,
                        style = MaterialTheme.typography.titleSmall,
                        color = MyDatesColors.textFieldLabelColor
                    )
                    //TODO show zodiac icon if active
                }
                Text(
                    text = data.event.type.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MyDatesColors.textFieldLabelColor
                )
                // TODO labels
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                data.formattedAge?.let { age ->
                    if (data.showTurnsWord) {
                        Text(
                            text = stringResource(R.string.event_age_label),
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.End,
                            color = MyDatesColors.textFieldLabelColor,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                    Text(
                        text = age,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.End,
                        color = MyDatesColors.textFieldLabelColor,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Text(
                    text = data.daysToEventText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    color = MyDatesColors.placeholderColor,
                    fontStyle = FontStyle.Italic,
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
        val eventItemData = EventItemData.fromEvent(
            event,
            context = LocalContext.current,
            isCompactAgeMode = false
        )
        EventItem(
            eventItemData = eventItemData,
            onNavigateToEventEditor = {}
        )
    }
}