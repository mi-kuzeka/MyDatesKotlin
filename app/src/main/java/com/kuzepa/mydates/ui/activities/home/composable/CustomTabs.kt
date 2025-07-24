package com.kuzepa.mydates.ui.activities.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTabs(
    tabList: List<String>,
    pagerState: PagerState,
    updateCurrentPage: (Int) -> Unit,
    coroutineScope: CoroutineScope
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        edgePadding = 0.dp,
        indicator = { },
        divider = {}
    ) {
        tabList.forEachIndexed { index, title ->
            val selected = pagerState.currentPage == index
            val modifier = Modifier
                .clickable(
                    // Remove ripple effect
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // onClick event
                    coroutineScope.launch {
                        updateCurrentPage(index)
                        pagerState.animateScrollToPage(index)
                    }
                }
            val backgroundColor = if (selected) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                Color.Unspecified
            }
            val shape = if (selected) {
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
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
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}