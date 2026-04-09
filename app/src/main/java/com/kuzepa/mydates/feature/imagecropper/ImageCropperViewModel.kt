package com.kuzepa.mydates.feature.imagecropper

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.common.util.onFailureIfNotCancelled
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.domain.usecase.image.SaveImageToCacheUseCase
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class ImageCropperViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val saveImageToCacheUseCase: SaveImageToCacheUseCase,
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val imageUriString: String? by lazy { savedStateHandle.get<String>("imageUriString") }
    private val _uiState = MutableStateFlow(ImageCropperUiState())
    val uiState: StateFlow<ImageCropperUiState> = _uiState.asStateFlow()

    private val logTag = "ImageCropperViewModel"

    init {
        imageUriString?.let { uriString ->
            val uri = try {
                uriString.toUri()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                viewModelScope.launch {
                    onError(
                        logMessage = getLogMessage(
                            logTag, "Error getting image from URI: $uriString", e
                        ),
                        showingMessage = context.resources.getString(R.string.error_getting_image)
                    )
                }
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
            saveImageToCacheUseCase(bitmap)
                .onSuccess { imagePath ->
                    navigationDialogResult.setDialogResultData(
                        DialogResultData.ImageCropperResult(imagePath)
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            savedImagePath = imagePath,
                            error = null
                        )
                    }
                }
                .onFailureIfNotCancelled { e ->
                    when (val errorMessage = e.message) {
                        null -> {
                            _uiState.update { it.copy(error = null) }
                        }

                        else -> {
                            onError(
                                logMessage = getLogMessage(
                                    logTag,
                                    "Image cropping error",
                                    errorMessage
                                ),
                                showingMessage = context.resources.getString(
                                    R.string.error_cropping_image
                                )
                            )
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(error = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    suspend fun onError(logMessage: String, showingMessage: String) {
        withContext((Dispatchers.IO)) {
            errorLoggerRepository.logError(logMessage)
        }
        withContext(Dispatchers.Main) {
            setError(showingMessage)
        }
    }
}