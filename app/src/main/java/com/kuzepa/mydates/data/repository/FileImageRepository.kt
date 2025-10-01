package com.kuzepa.mydates.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.kuzepa.mydates.domain.repository.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileImageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageRepository {
    override suspend fun saveBitmapToCache(
        bitmap: Bitmap,
        filename: String?
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val actualFilename = filename ?: "image_${System.currentTimeMillis()}.jpg"
                val cacheFile = File(context.cacheDir, actualFilename)

                FileOutputStream(cacheFile).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                        return@withContext Result.failure(Exception("Failed to compress bitmap"))
                    }
                }

                Result.success(cacheFile.absolutePath)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getBitmapFromCache(filename: String): Result<Bitmap?> {
        return withContext(Dispatchers.IO) {
            try {
                val cacheFile = File(filename)
                if (!cacheFile.exists()) {
                    return@withContext Result.success(null)
                }

                val bitmap = BitmapFactory.decodeFile(cacheFile.absolutePath)
                Result.success(bitmap)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteCachedImage(filename: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val cacheFile = File( filename)
                Result.success(cacheFile.delete())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun clearImageCache(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val cacheDir = context.cacheDir
                cacheDir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("image_") && file.name.endsWith(".jpg")) {
                        file.delete()
                    }
                }
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}