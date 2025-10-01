package com.kuzepa.mydates.domain.usecase.image

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.repository.ImageRepository
import javax.inject.Inject

class SaveImageToCacheUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, filename: String? = null): Result<String> {
        return imageRepository.saveBitmapToCache(bitmap, filename)
    }
}