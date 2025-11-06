package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.formatter.dateformat.FullDateFormat
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val appLanguageCode: Flow<String>
    suspend fun setAppLanguage(languageCode: String)
    val dateFormat: Flow<FullDateFormat>
    suspend fun setDateFormat(dateFormat: FullDateFormat)

    // Appearance
    val shouldShowDaysOfWeek: Flow<Boolean>
    suspend fun setShouldShowDaysOfWeek(showDaysOfWeek: Boolean)
    val shouldShowLabels: Flow<Boolean>
    suspend fun setShouldShowLabels(showLabels: Boolean)
    val shouldShowCompactAge: Flow<Boolean>
    suspend fun setShouldShowCompactAge(showCompactAge: Boolean)
    val shouldShowEventImages: Flow<Boolean>
    suspend fun setShouldShowEventImages(showEventImages: Boolean)
    val shouldShowPastEventsAtBottom: Flow<Boolean>
    suspend fun setShouldShowPastEventsAtBottom(showPastEventsAtBottom: Boolean)
    val getTheme: Flow<Int> // TODO set correct type
    suspend fun setTheme(theme: Int) // TODO set correct type

    // Notifications
// TODO add notification functions

    // Event date
    val shouldSetNoYearByDefault: Flow<Boolean>
    suspend fun setShouldSetNoYearByDefault(setNoYearByDefault: Boolean)
    val shouldSetTodayByDefault: Flow<Boolean>
    suspend fun setShouldSetTodayByDefault(setTodayByDefault: Boolean)
}