package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.domain.usecase.validation.rules.DateFormatRule
import com.kuzepa.mydates.domain.usecase.validation.rules.SelectionRequiredRule
import com.kuzepa.mydates.domain.usecase.validation.rules.TextFieldRequiredRule
import com.kuzepa.mydates.ui.common.DataStoreDateFormatProvider
import com.kuzepa.mydates.ui.common.ResourceValidationMessageProvider
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
    fun provideTextFieldRequiredRule(
        validationMessageProvider: ValidationMessageProvider
    ): TextFieldRequiredRule {
        return TextFieldRequiredRule(validationMessageProvider)
    }

    @Provides
    @Singleton
    fun provideDateFormatRule(
        validationMessageProvider: ValidationMessageProvider,
        dateFormatProvider: DateFormatProvider
    ): DateFormatRule {
        return DateFormatRule(validationMessageProvider, dateFormatProvider)
    }

    @Provides
    @Singleton
    fun provideSelectionRequiredRule(
        validationMessageProvider: ValidationMessageProvider
    ): SelectionRequiredRule {
        return SelectionRequiredRule(validationMessageProvider)
    }
}