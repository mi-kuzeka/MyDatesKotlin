package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.common.dateformat.DataStoreDateFormatProvider
import com.kuzepa.mydates.common.dateformat.DateFormatterWrapper
import com.kuzepa.mydates.common.userpreferences.DataStoreUserPreferencesProvider
import com.kuzepa.mydates.common.util.ResourceValidationMessageProvider
import com.kuzepa.mydates.data.local.manager.LocaleManagerImpl
import com.kuzepa.mydates.domain.formatter.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.manager.LocaleManager
import com.kuzepa.mydates.domain.repository.UserPreferencesRepository
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateSelectionRequiredUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideValidationMessageProvider(
        @ApplicationContext appContext: Context
    ): ValidationMessageProvider {
        return ResourceValidationMessageProvider(appContext)
    }

    @Provides
    @Singleton
    fun provideDateFormatProvider(
        @ApplicationContext appContext: Context
    ): DateFormatProvider {
        return DataStoreDateFormatProvider(appContext)
    }

    @Provides
    @Singleton
    fun provideDateFormatterWrapper(
        dateFormatter: DateFormatProvider
    ): DateFormatterWrapper {
        return DateFormatterWrapper(dateFormatter)
    }

    @Provides
    @Singleton
    fun provideValidateTextNotEmptyUseCase(
        validationMessageProvider: ValidationMessageProvider
    ): ValidateTextNotEmptyUseCase {
        return ValidateTextNotEmptyUseCase(validationMessageProvider)
    }

    @Provides
    @Singleton
    fun provideValidateDateUseCase(
        validationMessageProvider: ValidationMessageProvider,
        dateFormatProvider: DateFormatProvider
    ): ValidateDateUseCase {
        return ValidateDateUseCase(validationMessageProvider, dateFormatProvider)
    }

    @Provides
    @Singleton
    fun provideValidateSelectionRequiredUseCase(
        validationMessageProvider: ValidationMessageProvider
    ): ValidateSelectionRequiredUseCase {
        return ValidateSelectionRequiredUseCase(validationMessageProvider)
    }

    @Provides
    @Singleton
    fun provideValidateNameNotEmptyAndDistinctUseCase(
        validationMessageProvider: ValidationMessageProvider
    ): ValidateNameNotEmptyAndDistinctUseCase {
        return ValidateNameNotEmptyAndDistinctUseCase(validationMessageProvider)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return DataStoreUserPreferencesProvider(context)
    }

    @Provides
    @Singleton
    fun provideLocaleManager(
        userPreferencesRepository: UserPreferencesRepository,
        @ApplicationContext context: Context
    ): LocaleManager {
        return LocaleManagerImpl(userPreferencesRepository, context)
    }
}