package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.data.local.database.MyDatesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MyDatesDatabase {
        return MyDatesDatabase.buildDatabase(appContext)
    }

    @Provides
    fun provideEventDao(db: MyDatesDatabase) = db.eventDao()

    @Provides
    fun provideEventTypeDao(db: MyDatesDatabase) = db.eventTypeDao()

    @Provides
    fun provideLabelDao(db: MyDatesDatabase) = db.labelDao()
}