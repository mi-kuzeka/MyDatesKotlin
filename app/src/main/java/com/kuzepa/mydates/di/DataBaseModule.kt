package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.data.local.database.MyDatesDatabase
import com.kuzepa.mydates.data.local.database.MyDatesDatabaseInstance
import com.kuzepa.mydates.data.local.database.dao.EventTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        eventTypeProvider: Provider<EventTypeDao>
    ): MyDatesDatabase {
        return MyDatesDatabaseInstance.getInstance(appContext, eventTypeProvider)
    }

    @Provides
    fun provideEventDao(db: MyDatesDatabase) = db.eventDao()

    @Provides
    fun provideEventTypeDao(db: MyDatesDatabase) = db.eventTypeDao()

    @Provides
    fun provideLabelDao(db: MyDatesDatabase) = db.labelDao()

    @Provides
    fun provideEventLabelJoinDao(db: MyDatesDatabase) = db.eventLabelJoinDao()
}