package com.kuzepa.mydates.feature.home.event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.randomColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.components.chip.AddLabelChip
import com.kuzepa.mydates.ui.components.chip.LabelChip
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventLabelContainer(
    title: String,
    labels: List<Label>,
    onLabelClick: (id: String) -> Unit,
    onRemoveLabelClick: (id: String) -> Unit,
    buttonRemoveDescription: String,
    addLabelText: String,
    onAddLabelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.defaultContainerShape)
                .background(color = MyDatesColors.containerColor)
                .padding(
                    start = dimensionResource(R.dimen.padding_default),
                    end = dimensionResource(R.dimen.padding_default),
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_default)
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MyDatesColors.textFieldLabelColor
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                ),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                )
            ) {
                labels.forEach { label ->
                    key(label.id) {
                        LabelChip(
                            label = label,
                            onLabelClick = { onLabelClick(it) },
                            onRemoveLabelClick = onRemoveLabelClick,
                            buttonRemoveDescription = buttonRemoveDescription,
                        )
                    }
                }
                AddLabelChip(
                    addLabelText = addLabelText,
                    onAddLabelClick = onAddLabelClick
                )
            }
        }
        // Necessary to fill the remaining space to match all container styles
        MyDatesSupportingTextBox {

        }
    }
}

@Preview
@Composable
fun EventLabelContainerPreview() {
    MyDatesTheme {
        val labels: MutableList<Label> = remember {
            mutableListOf(
                Label(
                    id = "1",
                    name = "Friends",
                    color = 6,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 0
                ),
                Label(
                    id = "2",
                    name = "Family",
                    color = 3,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 10
                )
            )
        }
        var lastId = 2

        EventLabelContainer(
            title = "Tags",
            labels = labels,
            onLabelClick = {},
            onRemoveLabelClick = { labelId ->
                labels.removeAll { it.id == labelId }
            },
            buttonRemoveDescription = "Remove tag",
            addLabelText = "Add tag",
            onAddLabelClick = {
                labels.add(
                    Label(
                        id = (lastId++).toString(),
                        name = "Tag $lastId",
                        color = randomColor.toInt(),
                        notificationState = NotificationFilterState.FILTER_STATE_ON,
                        iconId = lastId
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}