package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun SwitchSegmentedButton(
    firstIsChecked: Boolean,
    firstVariant: String,
    secondVariant: String,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButton(
        selectedIndex = if (firstIsChecked) 0 else 1,
        options = listOf(firstVariant, secondVariant),
        onSelectedIndexChange = {
            onValueChange((it == 1))
        },
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Preview
@Composable
fun TwoVariantsSegmentedButtonSegmentedButtonPreview() {
    MyDatesTheme {
        var showYear by remember { mutableStateOf(true) }
        val fullMask = "mm/dd/yyyy"
        val shortMask = "mm/dd"
        SwitchSegmentedButton(
            firstIsChecked = showYear,
            firstVariant = fullMask,
            secondVariant = shortMask,
            onValueChange = { showYear = it }
        )
    }
}