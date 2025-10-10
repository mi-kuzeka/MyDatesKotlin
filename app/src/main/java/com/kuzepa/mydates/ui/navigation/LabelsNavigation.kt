package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.more.colorpicker.ColorPickerScreen
import com.kuzepa.mydates.feature.more.label.LabelScreen
import com.kuzepa.mydates.feature.more.label.labelchooser.LabelChooserScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Labels : NavRoute()

fun NavGraphBuilder.labelsDestination(
    onNavigateToLabelEditor: (id: String?, fromEvent: Boolean, showDeleteButton: Boolean) -> Unit,
    onNavigateToBulkLabelAssignment: (id: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Labels> {
//                LabelsScreen(
        //                onNavigateToLabelEditor = { onNavigateToLabelEditor(it, false, true },
        //                onNavigateToBulkLabelAssignment = onNavigateToBulkLabelAssignment
        //                )
    }
}

fun NavController.navigateToLabelChooser(eventLabelIdsJson: String) {
    navigate(route = LabelChooser(eventLabelIdsJson))
}

fun NavController.navigateToLabelEditor(
    id: String?,
    isOpenedFromEvent: Boolean,
    showDeleteButton: Boolean
) {
    navigate(route = LabelEditor(id, isOpenedFromEvent, showDeleteButton))
}

fun NavController.navigateToColorPicker(color: Int?) {
    navigate(route = ColorPicker(color = color))
}

fun NavController.navigateToBulkLabelAssignment(id: String) {
    navigate(route = BulkLabelAssignment(id = id))
}

@Serializable
internal data class LabelChooser(val eventLabelIdsJson: String) : NavRoute()

fun NavGraphBuilder.labelChooserDestination(
    onNavigateBack: () -> Unit,
    onNavigateToLabelEditor: (id: String?, fromEvent: Boolean, showDeleteButton: Boolean) -> Unit,
) {
    dialog<LabelChooser> { backStackEntry ->
        val labelChooser: LabelChooser = backStackEntry.toRoute()
        LabelChooserScreen(
            eventLabelIdsJson = labelChooser.eventLabelIdsJson,
            onNavigateBack = onNavigateBack,
            onNavigateToLabelEditor = { onNavigateToLabelEditor(it, false, false) },
        )
    }
}

@Serializable
internal data class LabelEditor(
    val id: String?,
    val isOpenedFromEvent: Boolean,
    val showDeleteButton: Boolean
) : NavRoute()

fun NavGraphBuilder.labelEditorDestination(
    onNavigateBack: () -> Unit,
    onNavigateToColorPicker: (color: Int?) -> Unit,
) {
    dialog<LabelEditor> { backStackEntry ->
        val labelEditor: LabelEditor = backStackEntry.toRoute()
        LabelScreen(
            id = labelEditor.id,
            isOpenedFromEvent = labelEditor.isOpenedFromEvent,
            showDeleteButton = labelEditor.showDeleteButton,
            onNavigateBack = onNavigateBack,
            onNavigateToColorPicker = onNavigateToColorPicker,
        )
    }
}

@Serializable
internal data class ColorPicker(val color: Int?) : NavRoute()

fun NavGraphBuilder.colorPickerDestination(
    onNavigateBack: () -> Unit
) {
    dialog<ColorPicker> { backStackEntry ->
        val colorPicker: ColorPicker = backStackEntry.toRoute()
        ColorPickerScreen(
            color = colorPicker.color,
            onNavigateBack = onNavigateBack
        )
    }
}

@Serializable
internal data class BulkLabelAssignment(val id: String) : NavRoute()

fun NavGraphBuilder.bulkLabelAssignmentDestination(onNavigateBack: () -> Unit) {
    composable<BulkLabelAssignment> { backStackEntry ->
        val bulkLabelAssignment: BulkLabelAssignment = backStackEntry.toRoute()
//        BulkLabelAssignmentScreen(currentLabelId = bulkLabelAssignment.id)
    }
}