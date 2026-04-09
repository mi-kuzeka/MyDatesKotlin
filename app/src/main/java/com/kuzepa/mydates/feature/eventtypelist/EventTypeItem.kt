package com.kuzepa.mydates.feature.eventtypelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.ui.theme.MyDatesColors

@Composable
fun EventTypeItem(
    eventType: EventType?,
    onNavigateToEventTypeEditor: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    eventType?.let { et ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.padding_small)
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onNavigateToEventTypeEditor(et.id) })
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .padding(all = dimensionResource(R.dimen.padding_default))
                .height(IntrinsicSize.Min),
        ) {
            Text(
                text = et.name,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MyDatesColors.textFieldLabelColor,
                modifier = Modifier.weight(1f),
            )
            if (et.isDefault) {
                Text(
                    text = stringResource(R.string.event_type_default_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MyDatesColors.accentTextColor,
                )
            }
        }
    }
}