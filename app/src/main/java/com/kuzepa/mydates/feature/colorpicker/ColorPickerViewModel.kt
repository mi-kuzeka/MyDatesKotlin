package com.kuzepa.mydates.feature.colorpicker

import androidx.core.graphics.ColorUtils
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.common.util.labelcolor.toColor
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.common.util.log.getLogMessage
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import com.kuzepa.mydates.ui.components.BaseViewModel
import com.kuzepa.mydates.ui.navigation.dialogresult.DialogResultData
import com.kuzepa.mydates.ui.navigation.dialogresult.NavigationDialogResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ColorPickerViewModel @Inject constructor(
    private val navigationDialogResult: NavigationDialogResult,
    private val errorLoggerRepository: ErrorLoggerRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ColorPickerScreenEvent>() {
    private val initialColor: Int? = savedStateHandle.get<Int>("color")
    private val _uiState = MutableStateFlow(ColorPickerUiState())
    val uiState: StateFlow<ColorPickerUiState> = _uiState.asStateFlow()

    private val logTag = "ColorPickerViewModel"

    init {
        when (initialColor) {
            null -> {
                runCatching {
                    ColorUtils.HSLToColor(
                        floatArrayOf(
                            Random.nextInt(360).toFloat(),
                            Random.nextFloat(),
                            Random.nextFloat()
                        )
                    ).toColor()
                }.onSuccess { randomColor ->
                    _uiState.update { it.copy(color = randomColor) }
                }.onFailure { e ->
                    viewModelScope.launch {
                        onError(getLogMessage(logTag, "Error setting random color", e))
                    }
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

    override suspend fun onError(logMessage: String, showingMessage: String?) {
        withContext((Dispatchers.IO)) {
            errorLoggerRepository.logError(logMessage)
        }
    }
}