package com.kuzepa.mydates.ui.components.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesErrorText
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.components.textfield.TextFieldLabel
import com.kuzepa.mydates.ui.components.textfield.TextFieldPlaceholder
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyDatesExposedDropDown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    options: List<String>,
    addNewItemLabel: String,
    onAddNewItem: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
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
                value = value,
                onValueChange = { },
                readOnly = true,
                colors = MyDatesColors.textFieldColors,
                label = {
                    TextFieldLabel(label)
                },
                placeholder = {
                    if (placeholder !== null) TextFieldPlaceholder(placeholder)
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = PrimaryNotEditable, enabled = true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = addNewItemLabel,
                            color = MyDatesColors.accentTextColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        expanded = false
                        onAddNewItem()
                    }
                )
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption,
                                color = MyDatesColors.defaultTextColor,
                                style = MaterialTheme.typography.bodyMedium
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
        MyDatesSupportingTextBox {
            errorMessage?.let {
                MyDatesErrorText(errorMessage)
            }
        }
    }
}

@Preview
@Composable
fun MyDatesExposedDropDownPreview() {
    MyDatesTheme {
        MyDatesExposedDropDown(
            value = "Birthday",
            onValueChange = {},
            label = "Event type",
            errorMessage = null,
            options = listOf("Birthday", "Anniversary"),
            addNewItemLabel = "Add new item",
            onAddNewItem = {}
        )
    }
}