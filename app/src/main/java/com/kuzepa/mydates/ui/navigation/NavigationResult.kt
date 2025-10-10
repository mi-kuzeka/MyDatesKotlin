package com.kuzepa.mydates.ui.navigation

import androidx.lifecycle.SavedStateHandle

object NavigationResult {
    const val OK = 1
    const val CANCEL = 0
    const val EVENT_TYPE_KEY = "event_type_navigation_result"
    const val EVENT_TYPE_ID_KEY = "event_type_id"
    const val LABEL_KEY = "label_navigation_result"
    const val LABEL_ID_KEY = "label_id"
    const val IMAGE_CROPPER_KEY = "image_cropper_navigation_result"
    const val IMAGE_PATH_KEY = "image_path"
    const val COLOR_PICKER_KEY = "color_picker_navigation_result"
    const val COLOR_KEY = "color"
    const val EVENT_KEY = "event_navigation_result"
    const val EVENT_MONTH_KEY = "event_month"
}

data class NavigationResultData(
    val result: Int?,
    val id: String? = null
)

data class ImageCropperNavigationResultData(
    val result: Int?,
    val imagePath: String? = null
)

data class ColorPickerNavigationResultData(
    val result: Int?,
    val color: Int?
)

// TODO remove if unused
internal fun removeNavigationResult(navigationKey: String, savedStateHandle: SavedStateHandle) {
    when (navigationKey) {
        NavigationResult.EVENT_TYPE_KEY -> {
            savedStateHandle.remove<Int>(NavigationResult.EVENT_TYPE_KEY)
            savedStateHandle.remove<String?>(NavigationResult.EVENT_TYPE_ID_KEY)
        }

        NavigationResult.LABEL_KEY -> {
            savedStateHandle.remove<Int>(NavigationResult.LABEL_KEY)
            savedStateHandle.remove<String?>(NavigationResult.LABEL_ID_KEY)
        }

        NavigationResult.IMAGE_CROPPER_KEY -> {
            savedStateHandle.remove<Int>(NavigationResult.IMAGE_CROPPER_KEY)
            savedStateHandle.remove<String?>(NavigationResult.IMAGE_PATH_KEY)
        }

        NavigationResult.COLOR_PICKER_KEY -> {
            savedStateHandle.remove<Int>(NavigationResult.COLOR_PICKER_KEY)
            savedStateHandle.remove<Int?>(NavigationResult.COLOR_KEY)
        }
    }
}