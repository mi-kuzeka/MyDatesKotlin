package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import kotlinx.serialization.Serializable

@Serializable
internal object DataTransfer : NavRoute()

fun NavGraphBuilder.dataTransferDestination(
    onNavigateToImportContacts: () -> Unit,
    onNavigateToImportCsv: () -> Unit,
    onNavigateToExportCsv: () -> Unit,
) {
    composable<DataTransfer> {
//                DataTransferScreen(
        //                onNavigateToImportContacts = onNavigateToImportContacts,
        //                onNavigateToImportCsv = onNavigateToImportCsv,
        //                onNavigateToExportCsv = onNavigateToExportCsv
        //                )
    }
}

fun NavController.navigateToImportContacts() {
    navigate(route = ImportContacts)
}

fun NavController.navigateToImportCsv() {
    navigate(route = ImportCsv)
}

fun NavController.navigateToExportCsv() {
    navigate(route = ExportCsv)
}

@Serializable
internal object ImportContacts : NavRoute()

fun NavGraphBuilder.importContactsDestination() {
    dialog<ImportContacts> {
//                ImportContactsScreen()
    }
}

@Serializable
internal object ImportCsv : NavRoute()

fun NavGraphBuilder.importCsvDestination() {
    dialog<ImportCsv> {
//                ImportCsvScreen()
    }
}

@Serializable
internal object ExportCsv : NavRoute()

fun NavGraphBuilder.exportCsvDestination() {
    dialog<ExportCsv> {
//                ExportCsvScreen()
    }
}