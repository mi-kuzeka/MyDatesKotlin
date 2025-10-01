package com.kuzepa.mydates.domain.usecase.image

import com.kuzepa.mydates.domain.repository.ImageRepository
import javax.inject.Inject

class DeleteCachedImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(filename: String): Result<Boolean> {
        return imageRepository.deleteCachedImage(filename)
    }
}