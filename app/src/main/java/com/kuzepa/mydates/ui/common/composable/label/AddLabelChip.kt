package com.kuzepa.mydates.ui.common.composable.label

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.common.composable.shape.MyDatesShapes
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun AddLabelChip(
    addLabelText: String,
    onAddLabelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InputChip(
        label = {
            Text(
                text = addLabelText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        selected = true,
        onClick = { onAddLabelClick() },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = addLabelText,
            )
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = MyDatesColors.actionChipContainerColor,
            containerColor = MyDatesColors.actionChipContainerColor,
            leadingIconColor = MyDatesColors.actionChipTextColor,
            selectedLeadingIconColor = MyDatesColors.actionChipTextColor,
            labelColor = MyDatesColors.actionChipTextColor,
            selectedLabelColor = MyDatesColors.actionChipTextColor,
        ),
        elevation = InputChipDefaults.inputChipElevation(
            elevation = 2.dp
        ),
        shape = MyDatesShapes.labelChipShape,
        modifier = modifier
    )
}

@Preview
@Composable
fun AddLabelChipPreview() {
    MyDatesTheme {
        AddLabelChip(
            addLabelText = "Add label",
            onAddLabelClick = {}
        )
    }
}