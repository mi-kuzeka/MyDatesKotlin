package com.kuzepa.mydates.ui.components.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun BaseContainer(
    containerTitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.defaultContainerShape)
                .background(color = MyDatesColors.containerColor)
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_default),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(
                text = containerTitle,
                style = MaterialTheme.typography.labelMedium,
                color = MyDatesColors.textFieldLabelColor
            )
            content()
        }
        // Necessary to fill the remaining space to match all container styles
        MyDatesSupportingTextBox {

        }
    }
}