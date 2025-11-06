package com.kuzepa.mydates.ui.components.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.domain.model.label.IconType
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import com.kuzepa.mydates.ui.components.chip.IconChip
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelDropDown(
    label: Label?,
    onValueChange: (Label) -> Unit,
    options: List<Label>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
            .clip(Shapes.buttonShape)
            .background(MyDatesColors.containerColor)
    ) {
        LabelDropDownMenuItem(
            label = label,
            expanded = expanded,
            modifier = Modifier
                .menuAnchor(type = PrimaryNotEditable, enabled = true)
                .padding(dimensionResource(R.dimen.padding_default))
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        LabelDropDownMenuItem(label = selectionOption)
                    },
                    onClick = {
                        onValueChange(selectionOption)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(MyDatesColors.containerColor)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelDropDownMenuItem(
    label: Label?,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        label?.let {
            val labelColor = LabelColor.getColorFromId(label.color)
            val labelIconColor = labelColor.getContrastedColor()
            val labelIcon = LabelIcon.fromId(label.iconId)
            IconChip(
                chipSize = dimensionResource(R.dimen.default_icon_size),
                color = labelColor,
                iconType = labelIcon?.iconType ?: IconType.FIRST_LETTER,
                firstLetter = label.name.take(1),
                iconDrawableResId = labelIcon?.drawableRes,
                iconColor = labelIconColor
            )
        }
        Text(
            text = label?.name ?: "",
            color = MyDatesColors.defaultTextColor,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
        )
        expanded?.let {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
    }
}

@Preview
@Composable
fun MyDatesDropDownPreview() {
    MyDatesTheme {
        LabelDropDown(
            label = Label(
                id = "1",
                name = "Friends",
                color = 3,
                notificationState = NotificationFilterState.FILTER_STATE_ON,
                iconId = LabelIcon.FIRST_LETTER.id
            ),
            onValueChange = {},
            options = listOf(
                Label(
                    id = "1",
                    name = "Friends",
                    color = 3,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = LabelIcon.CONSTRUCTION.id
                ),
                Label(
                    id = "2",
                    name = "Others",
                    color = 6,
                    notificationState = NotificationFilterState.FILTER_STATE_OFF,
                    iconId = LabelIcon.KEY_ICON.id
                )
            ),
        )
    }
}