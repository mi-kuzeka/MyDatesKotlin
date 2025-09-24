package com.kuzepa.mydates.ui.components.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelDropDown(
    label: Label?,
    onValueChange: (Label) -> Unit,
    options: List<Label>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = label?.name ?: "",
                onValueChange = { },
                readOnly = true,
                colors = MyDatesColors.textFieldColors,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = PrimaryNotEditable, enabled = true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption.name,
                                color = MyDatesColors.defaultTextColor,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
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
                iconId = LabelIcon.CONSTRUCTION.id
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