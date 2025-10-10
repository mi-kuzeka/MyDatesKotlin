package com.kuzepa.mydates.feature.more.colorpicker

import androidx.core.graphics.ColorUtils
import androidx.lifecycle.SavedStateHandle
import com.kuzepa.mydates.common.util.labelcolor.toColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.ui.components.BaseViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ColorPickerViewModel @Inject constructor(
    private val navigationDialogResult: NavigationDialogResult,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ColorPickerScreenEvent>() {
    private val initialColor: Int? = savedStateHandle.get<Int>("color")
    private val _uiState = MutableStateFlow(ColorPickerUiState())
    val uiState: StateFlow<ColorPickerUiState> = _uiState.asStateFlow()

    init {
        when (initialColor) {
            null -> {
                try {
                    val randomColor = ColorUtils.HSLToColor(
                        floatArrayOf(
                            Random.nextInt(360).toFloat(),
                            Random.nextFloat(),
                            Random.nextFloat()
                        )
                    ).toColor()
                    _uiState.update { it.copy(color = randomColor) }
                } catch (_: Exception) {
                }
            }

            else -> _uiState.update { it.copy(color = initialColor.toColor()) }
        }
    }

    override fun onEvent(event: ColorPickerScreenEvent) {
        when (event) {
            is ColorPickerScreenEvent.ColorChanged -> {
                _uiState.update {
                    it.copy(color = event.color)
                }
            }

            is ColorPickerScreenEvent.SetDialogResult -> {
                navigationDialogResult.setDialogResultData(
                    DialogResultData.ColorPickerResult(_uiState.value.color.toInt())
                )
            }
        }
    }
}