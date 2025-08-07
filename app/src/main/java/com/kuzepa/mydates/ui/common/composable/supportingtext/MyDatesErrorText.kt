package com.kuzepa.mydates.ui.common.composable.supportingtext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors

@Composable
fun MyDatesErrorText(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    MyDatesSupportingText(
        text = errorMessage,
        color = MyDatesColors.errorTextColor,
        modifier = modifier
    )
}