package com.kuzepa.mydates.di

import com.kuzepa.mydates.data.repository.FileErrorLoggerRepository
import com.kuzepa.mydates.domain.manager.LogFileManager
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {
    @Provides
    @Singleton
    fun provideErrorLogger(
        logFileManager: LogFileManager
    ): ErrorLoggerRepository = FileErrorLoggerRepository(logFileManager)
}