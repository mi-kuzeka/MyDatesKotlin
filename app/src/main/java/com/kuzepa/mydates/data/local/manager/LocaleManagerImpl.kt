package com.kuzepa.mydates.data.local.manager

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.manager.Language
import com.kuzepa.mydates.domain.manager.LocaleManager
import com.kuzepa.mydates.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class LocaleManagerImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @param:ApplicationContext private val context: Context
) : LocaleManager {
    private val localeFlow = MutableStateFlow(Locale.getDefault())

    override val currentLocale: Flow<Locale>
        get() = localeFlow

    override val currentLanguageCode: Flow<String>
        get() = localeFlow.map { it.language }

    override suspend fun setLocale(languageCode: String) {
        userPreferencesRepository.setAppLanguage(languageCode)

        updateLocaleByLanguageCode(languageCode)

        val localeListCompat = LocaleListCompat.create(localeFlow.value)
        AppCompatDelegate.setApplicationLocales(localeListCompat)
    }

    private fun updateLocaleByLanguageCode(languageCode: String) {
        if (TextUtils.isEmpty(languageCode)) {
            localeFlow.value = Resources.getSystem().configuration.getLocales().get(0)
        } else {
            localeFlow.value = Locale.forLanguageTag(languageCode)
        }
    }

    override fun getDisplayLanguage(languageCode: String): String {
        return localeFlow.value.getDisplayLanguage(localeFlow.value)
    }

    override fun getSupportedLanguages(): List<Language> {
        return listOf(
            Language(
                code = "",
                displayName = "Default",
                nativeName = context.resources.getString(R.string.default_locale_title)
            ),
            Language("de", "German", "Deutsch"),
            Language("en", "English", "English"),
            Language("es", "Spanish", "Español"),
            Language("fr", "French", "Français"),
            Language("it", "Italian", "Italiano"),
            Language("ru", "Russian", "Русский")
        )
    }
}