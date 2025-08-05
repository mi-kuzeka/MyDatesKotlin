package com.kuzepa.mydates.ui.common.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun SingleChoiceSegmentedButton(
    selectedIndex: Int,
    options: List<String>,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(8.dp)
                ),
                onClick = { onSelectedIndexChange(index) },
                selected = index == selectedIndex,
                label = {
                    Text(
                        label,
                        style = textStyle
                    )
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    activeBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    inactiveBorderColor = MaterialTheme.colorScheme.surfaceContainer,
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    inactiveContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Preview
@Composable
fun SingleChoiceSegmentedButtonPreview() {
    MyDatesTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }
        val options = listOf("Day", "Month")
        SingleChoiceSegmentedButton(
            selectedIndex = selectedIndex,
            options = options,
            onSelectedIndexChange = { selectedIndex == it }
        )
    }
}