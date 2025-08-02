package com.kuzepa.mydates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import kotlinx.serialization.Serializable

@Serializable
internal object DataTransfer : NavRoute()

fun NavGraphBuilder.dataTransferDestination(
    onNavigateToContactsImport: () -> Unit,
    onNavigateToCsvImport: () -> Unit,
    onNavigateToCsvExport: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<DataTransfer> {
//                DataTransferScreen(
        //                onNavigateToContactsImport = onNavigateToContactsImport,
        //                onNavigateToCsvImport = onNavigateToCsvImport,
        //                onNavigateToCsvExport = onNavigateToCsvExport
        //                )
    }
}

fun NavController.navigateToContactsImport() {
    navigate(route = ContactsImport)
}

fun NavController.navigateToCsvImport() {
    navigate(route = CsvImport)
}

fun NavController.navigateToCsvExport() {
    navigate(route = CsvExport)
}

@Serializable
internal object ContactsImport : NavRoute()

fun NavGraphBuilder.contactsImportDestination(onNavigateBack: () -> Unit) {
    dialog<ContactsImport> {
//                ContactsImportScreen()
    }
}

@Serializable
internal object CsvImport : NavRoute()

fun NavGraphBuilder.csvImportDestination(onNavigateBack: () -> Unit) {
    dialog<CsvImport> {
//                CsvImportScreen()
    }
}

@Serializable
internal object CsvExport : NavRoute()

fun NavGraphBuilder.csvExportDestination(onNavigateBack: () -> Unit) {
    dialog<CsvExport> {
//                CsvExportScreen()
    }
}