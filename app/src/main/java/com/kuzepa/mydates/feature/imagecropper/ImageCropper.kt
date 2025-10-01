package com.kuzepa.mydates.feature.imagecropper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.kuzepa.mydates.common.util.labelcolor.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Stable
class ImageCropperState() {
    internal var cropImageView by mutableStateOf<CropImageView?>(null)
        private set

    private var uri: Uri = Uri.EMPTY

    // Configurations
    var aspectRatio by mutableStateOf(1 to 1)
    var isAutoZoomEnabled by mutableStateOf(true)
    var isFixAspectRatio by mutableStateOf(true)
    var cropShape by mutableStateOf(CropImageView.CropShape.RECTANGLE)
    var guidelines by mutableStateOf(CropImageView.Guidelines.ON)
    var showProgressBar by mutableStateOf(true)

    fun load(imageUri: Uri) {
        uri = imageUri
        if (cropImageView?.imageUri != imageUri) {
            cropImageView?.setImageUriAsync(imageUri)
        }
    }

    suspend fun crop(
        reqWidth: Int = 256,
        reqHeight: Int = 256,
        options: CropImageView.RequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_INSIDE
    ): Bitmap? = withContext(Dispatchers.IO) {
        cropImageView?.getCroppedImage(reqWidth, reqHeight, options)
    }

    fun rotateLeft() {
        cropImageView?.rotateImage(-90)
    }

    fun rotateRight() {
        cropImageView?.rotateImage(90)
    }

    internal fun applyConfig() {
        cropImageView?.setAspectRatio(aspectRatio.first, aspectRatio.second)
        cropImageView?.setFixedAspectRatio(isFixAspectRatio)
        cropImageView?.isAutoZoomEnabled = isAutoZoomEnabled
        cropImageView?.cropShape = cropShape
        cropImageView?.guidelines = guidelines
        cropImageView?.isShowProgressBar = showProgressBar
    }

    internal fun viewFactory(context: Context, primaryColor: Int): FrameLayout {
        val layout = FrameLayout(context)
        val cropperView = CropImageView(context)

        // Apply initial configuration
        cropperView.setAspectRatio(aspectRatio.first, aspectRatio.second)
        cropperView.setFixedAspectRatio(isFixAspectRatio)
        cropperView.isAutoZoomEnabled = isAutoZoomEnabled
        cropperView.cropShape = cropShape
        cropperView.guidelines = guidelines
        cropperView.isShowProgressBar = showProgressBar
        cropperView.setImageCropOptions(
            CropImageOptions().apply {
                guidelinesColor = primaryColor
                borderLineColor = primaryColor
                borderCornerColor = primaryColor
            }
        )

        layout.addView(
            cropperView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
        )
        cropImageView = cropperView
        return layout
    }

    internal fun viewUpdate(view: FrameLayout) {
        if (uri != Uri.EMPTY) {
            load(uri)
        }
        applyConfig()
    }

    internal fun viewRelease(view: FrameLayout) {
        cropImageView?.clearImage()
        cropImageView = null
    }
}

@Composable
fun rememberImageCropperState(uri: Uri? = null): ImageCropperState {
    val state = remember { ImageCropperState() }

    if (uri != null) {
        LaunchedEffect(uri) {
            state.load(uri)
        }

        LaunchedEffect(
            state.aspectRatio,
            state.isAutoZoomEnabled,
            state.isFixAspectRatio,
            state.cropShape,
            state.guidelines,
            state.showProgressBar
        ) {
            state.applyConfig()
        }
    }

    return state
}

@Composable
fun ImageCropper(
    state: ImageCropperState,
    modifier: Modifier = Modifier,
) {
    // TODO handle configuration changes
    Box(modifier = modifier) {
        val primaryColor = MaterialTheme.colorScheme.primary.toInt()
        val context = LocalContext.current
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { state.viewFactory(context, primaryColor) },
            update = state::viewUpdate,
            onRelease = state::viewRelease
        )
    }
}