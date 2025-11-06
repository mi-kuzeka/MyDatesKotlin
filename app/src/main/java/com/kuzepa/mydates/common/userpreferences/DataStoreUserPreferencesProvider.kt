package com.kuzepa.mydates.common.userpreferences

import android.content.Context
import com.kuzepa.mydates.domain.formatter.dateformat.FullDateFormat
import com.kuzepa.mydates.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DataStoreUserPreferencesProvider @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferencesRepository {

    override val appLanguageCode: Flow<String>
        // TODO get from DataStore
        get() = flowOf("en")

    override suspend fun setAppLanguage(languageCode: String) {
        TODO("Not yet implemented")
    }

    override val dateFormat: Flow<FullDateFormat>
        get() = TODO("Not yet implemented")

    override suspend fun setDateFormat(dateFormat: FullDateFormat) {
        TODO("Not yet implemented")
    }

    override val shouldShowDaysOfWeek: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldShowDaysOfWeek(showDaysOfWeek: Boolean) {
        TODO("Not yet implemented")
    }

    override val shouldShowLabels: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldShowLabels(showLabels: Boolean) {
        TODO("Not yet implemented")
    }

    override val shouldShowCompactAge: Flow<Boolean>
        // TODO get actual value
        get() = flowOf(false)

    override suspend fun setShouldShowCompactAge(showCompactAge: Boolean) {
        TODO("Not yet implemented")
    }

    override val shouldShowEventImages: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldShowEventImages(showEventImages: Boolean) {
        TODO("Not yet implemented")
    }

    override val shouldShowPastEventsAtBottom: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldShowPastEventsAtBottom(showPastEventsAtBottom: Boolean) {
        TODO("Not yet implemented")
    }

    override val getTheme: Flow<Int>
        get() = TODO("Not yet implemented")

    override suspend fun setTheme(theme: Int) {
        TODO("Not yet implemented")
    }

    override val shouldSetNoYearByDefault: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldSetNoYearByDefault(setNoYearByDefault: Boolean) {
        TODO("Not yet implemented")
    }

    override val shouldSetTodayByDefault: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldSetTodayByDefault(setTodayByDefault: Boolean) {
        TODO("Not yet implemented")
    }

}