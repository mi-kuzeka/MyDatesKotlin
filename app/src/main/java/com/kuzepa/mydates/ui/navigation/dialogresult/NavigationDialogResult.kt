package com.kuzepa.mydates.ui.navigation.dialogresult

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDialogResult @Inject constructor() {
    private val _dialogResultData: MutableStateFlow<DialogResultData?> = MutableStateFlow(null)
    val dialogResultData = _dialogResultData.asStateFlow()

    fun setDialogResultData(resultData: DialogResultData) {
        _dialogResultData.update { resultData }
    }

    fun clearDialogResultData() {
        _dialogResultData.update { null }
    }
}


sealed class DialogResultData {
    data class EventLabelResult(val id: String?) : DialogResultData()
    data class LabelResult(val id: String?) : DialogResultData()
    data class ImageCropperResult(val imagePath: String?) : DialogResultData()
    data class EventTypeResult(val id: String?) : DialogResultData()
    data class ColorPickerResult(val color: Int?) : DialogResultData()
}