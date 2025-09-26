package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.more.label.LabelScreen
import com.kuzepa.mydates.feature.more.label.labelchooser.LabelChooserScreen
import kotlinx.serialization.Serializable

@Serializable
internal object Labels : NavRoute()

fun NavGraphBuilder.labelsDestination(
    onNavigateToLabelEditor: (id: String?) -> Unit,
    onNavigateToBulkLabelAssignment: (id: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Labels> {
//                LabelsScreen(
        //                onNavigateToLabelEditor = onNavigateToLabelEditor,
        //                onNavigateToBulkLabelAssignment = onNavigateToBulkLabelAssignment
        //                )
    }
}

fun NavController.navigateToLabelChooser(eventLabelIdsJson: String) {
    navigate(route = LabelChooser(eventLabelIdsJson))
}

fun NavController.navigateToLabelEditor(
    id: String?,
    isOpenedFromEvent: Boolean
) {
    navigate(route = LabelEditor(id = id, isOpenedFromEvent = isOpenedFromEvent))
}

fun NavController.navigateToBulkLabelAssignment(id: String) {
    navigate(route = BulkLabelAssignment(id = id))
}

@Serializable
internal data class LabelChooser(val eventLabelIdsJson: String) : NavRoute()

fun NavGraphBuilder.labelChooserDestination(
    onNavigateBack: (result: Int, id: String?) -> Unit,
    onNavigateToLabelEditor: (id: String?) -> Unit,
) {
    dialog<LabelChooser> { backStackEntry ->
        val labelChooser: LabelChooser = backStackEntry.toRoute()
        val savedStateHandle = backStackEntry.savedStateHandle

        val labelNavigationResult =
            savedStateHandle.get<Int>(NavigationResult.LABEL_KEY)
        val labelId = savedStateHandle.get<String?>(NavigationResult.LABEL_ID_KEY)

        LabelChooserScreen(
            eventLabelIdsJson = labelChooser.eventLabelIdsJson,
            onNavigateBack = onNavigateBack,
            onNavigateToLabelEditor = onNavigateToLabelEditor,
            labelNavigationResult = NavigationResultData(
                result = labelNavigationResult,
                id = labelId
            ),
            removeNavigationResult = {
                savedStateHandle.remove<Int>(NavigationResult.LABEL_KEY)
                savedStateHandle.remove<String?>(NavigationResult.LABEL_ID_KEY)
            }
        )
    }
}

@Serializable
internal data class LabelEditor(
    val id: String?,
    val isOpenedFromEvent: Boolean
) : NavRoute()

fun NavGraphBuilder.labelEditorDestination(
    onNavigateBack: (result: Int, id: String?) -> Unit
) {
    dialog<LabelEditor> { backStackEntry ->
        val labelEditor: LabelEditor = backStackEntry.toRoute()
        LabelScreen(
            id = labelEditor.id,
            isOpenedFromEvent = labelEditor.isOpenedFromEvent,
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