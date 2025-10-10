package com.kuzepa.mydates.feature.imagecropper

import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.domain.usecase.image.SaveImageToCacheUseCase
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageCropperViewModel @Inject constructor(
    private val saveImageToCacheUseCase: SaveImageToCacheUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val imageUriString: String? by lazy { savedStateHandle.get<String>("imageUriString") }
    private val _uiState = MutableStateFlow(ImageCropperUiState())
    val uiState: StateFlow<ImageCropperUiState> = _uiState.asStateFlow()

    init {
        imageUriString?.let { uriString ->
            val uri = try {
                uriString.toUri()
            } catch (_: Exception) {
                null
            }
            _uiState.update {
                it.copy(imageUri = uri)
            }
        }
    }

    fun onCropImage(bitmap: Bitmap) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = saveImageToCacheUseCase(bitmap)
            if (result.isSuccess) {
                val imagePath = result.getOrDefault(null)
                navigationDialogResult.setDialogResultData(DialogResultData.ImageCropperResult(imagePath))
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        savedImagePath = imagePath,
                        error = null
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}