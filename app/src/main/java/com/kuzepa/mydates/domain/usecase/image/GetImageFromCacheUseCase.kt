package com.kuzepa.mydates.domain.usecase.image

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageFromCacheUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(filename: String): Result<Bitmap?> {
        return imageRepository.getBitmapFromCache(filename)
    }
}