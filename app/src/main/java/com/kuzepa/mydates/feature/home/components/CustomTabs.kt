package com.kuzepa.mydates.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTabs(
    tabList: List<String>,
    currentPage: Int,
    updateCurrentPage: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = currentPage,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        edgePadding = 0.dp,
        indicator = { },
        divider = {},
        modifier = modifier
    ) {
        tabList.forEachIndexed { index, title ->
            val selected = currentPage == index
            val modifier = Modifier
                .clickable(
                    // Remove ripple effect
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // onClick event
                    coroutineScope.launch {
                        updateCurrentPage(index)
                    }
                }
            val backgroundColor = if (selected) {
                MyDatesColors.containerColor
            } else {
                Color.Unspecified
            }
            val shape = if (selected) {
                Shapes.selectedTabShape
            } else {
                RectangleShape
            }
            // Custom tab
            Box(
                modifier = modifier
                    .clip(shape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}