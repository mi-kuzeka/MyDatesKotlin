package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.data.local.file.LogFileManagerImpl
import com.kuzepa.mydates.domain.manager.LogFileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    @Singleton
    fun provideLogFileManager(
        @ApplicationContext context: Context
    ): LogFileManager {
        return LogFileManagerImpl(context)
    }
}