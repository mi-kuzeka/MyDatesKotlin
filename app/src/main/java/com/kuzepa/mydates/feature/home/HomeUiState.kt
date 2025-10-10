package com.kuzepa.mydates.feature.home

import androidx.compose.runtime.Immutable
import com.kuzepa.mydates.domain.model.MonthPager

@Immutable
data class HomeUiState(
    val currentPage: Int = MonthPager.FIRST_PAGE,
    val tabs: List<String> = emptyList()
)