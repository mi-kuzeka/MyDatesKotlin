package com.kuzepa.mydates.ui.components.supportingtext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kuzepa.mydates.ui.theme.MyDatesColors

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