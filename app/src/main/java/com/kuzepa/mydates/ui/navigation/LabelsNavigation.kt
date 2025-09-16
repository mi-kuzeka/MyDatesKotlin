package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.kuzepa.mydates.feature.more.label.LabelScreen
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

fun NavController.navigateToLabelEditor(id: String?) {
    navigate(route = LabelEditor(id = id))
}

fun NavController.navigateToBulkLabelAssignment(id: String) {
    navigate(route = BulkLabelAssignment(id = id))
}

@Serializable
internal data class LabelEditor(val id: String?) : NavRoute()

fun NavGraphBuilder.labelEditorDestination(onNavigateBack: (result: Int, id: String?) -> Unit) {
    dialog<LabelEditor> { backStackEntry ->
        val labelEditor: LabelEditor = backStackEntry.toRoute()
        LabelScreen(id = labelEditor.id, onNavigateBack = onNavigateBack)
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