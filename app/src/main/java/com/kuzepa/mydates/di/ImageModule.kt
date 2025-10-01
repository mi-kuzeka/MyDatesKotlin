package com.kuzepa.mydates.di

import android.content.Context
import com.kuzepa.mydates.data.repository.FileImageRepository
import com.kuzepa.mydates.domain.repository.ImageRepository
import com.kuzepa.mydates.domain.usecase.image.DeleteCachedImageUseCase
import com.kuzepa.mydates.domain.usecase.image.GetImageFromCacheUseCase
import com.kuzepa.mydates.domain.usecase.image.SaveImageToCacheUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {
    @Provides
    @Singleton
    fun provideImageRepository(@ApplicationContext context: Context): ImageRepository {
        return FileImageRepository(context)
    }

    @Provides
    @Singleton
    fun provideSaveImageToCacheUseCase(imageRepository: ImageRepository): SaveImageToCacheUseCase {
        return SaveImageToCacheUseCase(imageRepository)
    }

    @Provides
    @Singleton
    fun provideGetImageFromCacheUseCase(imageRepository: ImageRepository): GetImageFromCacheUseCase {
        return GetImageFromCacheUseCase(imageRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteCachedImageUseCase(imageRepository: ImageRepository): DeleteCachedImageUseCase {
        return DeleteCachedImageUseCase(imageRepository)
    }
}