package com.kuzepa.mydates.domain.manager

import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LocaleManager {
    val currentLocale: Flow<Locale>
    val currentLanguageCode: Flow<String>
    suspend fun setLocale(languageCode: String)
    fun getDisplayLanguage(languageCode: String): String
    fun getSupportedLanguages(): List<Language>
}

data class Language(
    val code: String,
    val displayName: String,
    val nativeName: String
)