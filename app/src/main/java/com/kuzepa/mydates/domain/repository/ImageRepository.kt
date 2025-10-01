package com.kuzepa.mydates.domain.repository

import android.graphics.Bitmap

interface ImageRepository {
    suspend fun saveBitmapToCache(bitmap: Bitmap, filename: String? = null): Result<String> // Returns file path
    suspend fun getBitmapFromCache(filename: String): Result<Bitmap?>
    suspend fun deleteCachedImage(filename: String): Result<Boolean>
    suspend fun clearImageCache(): Result<Boolean>
}