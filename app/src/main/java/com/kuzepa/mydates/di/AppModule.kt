package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.domain.dateformat.DateFormatProvider
import com.kuzepa.mydates.domain.usecase.validation.ValidationMessageProvider
import com.kuzepa.mydates.ui.common.ResourceDateFormatProvider
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
        return ResourceDateFormatProvider(appContext)
    }
}