package com.kuzepa.mydates.ui.components.chip

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

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
        onClick = onAddLabelClick,
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
        shape = Shapes.labelChipShape,
        elevation = InputChipDefaults.inputChipElevation(
            elevation = 2.dp
        ),
        modifier = modifier
            .height(dimensionResource(R.dimen.color_chip_size))
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