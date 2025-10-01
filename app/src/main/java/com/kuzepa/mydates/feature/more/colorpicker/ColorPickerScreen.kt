package com.kuzepa.mydates.feature.more.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.toInt
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorDialog
import com.kuzepa.mydates.ui.navigation.NavigationResult
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorPickerScreen(
    viewModel: ColorPickerViewModel = hiltViewModel(),
    color: Int?,
    onNavigateBack: (result: Int, color: Int?) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BaseEditorDialog(
        title = stringResource(R.string.color_picker_title),
        isNewItem = false,
        hasChanges = false,
        onNavigateBack = { onNavigateBack(NavigationResult.CANCEL, null) },
        onSave = { onNavigateBack(NavigationResult.OK, state.color.toInt()) },
        showDeleteButton = false,
        scrollBehavior = scrollBehavior
    ) {
        ColorPickerScreenContent(
            color = state.color,
            onColorChanged = { newColor ->
                viewModel.onEvent(ColorPickerScreenEvent.ColorChanged(newColor))
            }
        )
    }
}

@Composable
internal fun ColorPickerScreenContent(
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val controller = rememberColorPickerController()
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_default),
            alignment = Alignment.Top
        ),
        modifier = modifier
            .wrapContentSize()
            .verticalScroll(state = scrollState)
            .padding(dimensionResource(R.dimen.padding_default))
    ) {
        HsvColorPicker(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .heightIn(max = 350.dp)
                .aspectRatio(1f)
                .fillMaxWidth(0.9f),
            controller = controller,
            initialColor = color,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                if (colorEnvelope.fromUser) onColorChanged(colorEnvelope.color)
            }
        )
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            controller = controller,
            initialColor = color,
        )
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.color_preview_size))
                .clip(Shapes.defaultDialogShape)
                .background(color)
        )
    }
}

@Preview
@Composable
fun ColorPickerScreenPreview() {
    var color = Color.Yellow
    MyDatesTheme {
        ColorPickerScreenContent(
            color = color,
            onColorChanged = { color = it }
        )
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun ColorPickerScreenPreviewLandscape() {
    var color = Color.Blue
    MyDatesTheme {
        ColorPickerScreenContent(
            color = color,
            onColorChanged = { color = it }
        )
    }
}