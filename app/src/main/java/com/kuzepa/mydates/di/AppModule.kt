package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateDateUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateNameNotEmptyAndDistinctUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateSelectionRequiredUseCase
import com.kuzepa.mydates.domain.usecase.validation.rules.ValidateTextNotEmptyUseCase
import com.kuzepa.mydates.ui.common.utils.ResourceValidationMessageProvider
import com.kuzepa.mydates.ui.common.utils.dateformat.DataStoreDateFormatProvider
import com.kuzepa.mydates.ui.common.utils.dateformat.DateFormatterWrapper
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
}