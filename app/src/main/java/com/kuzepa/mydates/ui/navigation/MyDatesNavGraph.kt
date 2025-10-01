package com.kuzepa.mydates.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MyDatesNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) },
        modifier = modifier
    ) {
        eventsDestination(onNavigateToEventEditor = { eventId ->
            navController.navigateToEventEditor(id = eventId)
        })
        eventEditorDestination(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEventTypeCreator = { navController.navigateToEventTypeEditor(null) },
            onNavigateToLabelChooser = { eventIds -> navController.navigateToLabelChooser(eventIds) },
            onNavigateToLabelEditor = { labelId ->
                navController.navigateToLabelEditor(id = labelId, isOpenedFromEvent = true)
            },
            onNavigateToImageCropper = { imageUriString ->
                navController.navigateToImageCropper(imageUriString = imageUriString)
            }
        )
        imageCropperDestination(onNavigateBack = { navigationResult, filePath ->
            navController.popBackStack()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.apply {
                    set(NavigationResult.IMAGE_CROPPER_KEY, navigationResult)
                    filePath?.let { set(NavigationResult.IMAGE_PATH_KEY, it) }
                }
        })

        appearanceDestination()

        searchDestination()

        moreDestination(
            onNavigateToEventTypes = { navController.navigateToEventTypes() },
            onNavigateToLabels = { navController.navigateToLabels() },
            onNavigateToDataTransfer = { navController.navigateToDataTransfer() },
            onNavigateToSettings = { navController.navigateToSettings() },
            onNavigateToDonation = { navController.navigateToDonation() },
            onNavigateToHelp = { navController.navigateToHelp() },
            onNavigateToAbout = { navController.navigateToAbout() },
        )
        eventTypesDestination(
            onNavigateToEventTypeEditor = { eventTypeId ->
                navController.navigateToEventTypeEditor(eventTypeId)
            },
            onNavigateBack = { navController.popBackStack() }
        )
        eventTypeEditorDestination(onNavigateBack = { navigationResult, id ->
            navController.popBackStack()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.apply {
                    set(NavigationResult.EVENT_TYPE_KEY, navigationResult)
                    id?.let { set(NavigationResult.EVENT_TYPE_ID_KEY, it) }
                }
        })

        labelsDestination(
            onNavigateToLabelEditor = { labelId ->
                navController.navigateToLabelEditor(labelId, isOpenedFromEvent = false)
            },
            onNavigateToBulkLabelAssignment = { labelId ->
                navController.navigateToBulkLabelAssignment(labelId)
            },
            onNavigateBack = { navController.popBackStack() }
        )
        labelEditorDestination(
            onNavigateBack = { navigationResult, id ->
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.apply {
                        set(NavigationResult.LABEL_KEY, navigationResult)
                        id?.let { set(NavigationResult.LABEL_ID_KEY, it) }
                    }
            },
            onNavigateToColorPicker = { color ->
                navController.navigateToColorPicker(color)
            }
        )
        colorPickerDestination(
            onNavigateBack = { navigationResult, color ->
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.apply {
                        set(NavigationResult.COLOR_PICKER_KEY, navigationResult)
                        color?.let { set(NavigationResult.COLOR_KEY, it) }
                    }
            }
        )
        labelChooserDestination(
            onNavigateBack = { navigationResult, id ->
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.apply {
                        set(NavigationResult.LABEL_KEY, navigationResult)
                        id?.let { set(NavigationResult.LABEL_ID_KEY, it) }
                    }
            },
            onNavigateToLabelEditor = { labelId ->
                navController.navigateToLabelEditor(id = labelId, isOpenedFromEvent = true)
            }
        )
        bulkLabelAssignmentDestination(onNavigateBack = { navController.popBackStack() })

        dataTransferDestination(
            onNavigateToContactsImport = { navController.navigateToContactsImport() },
            onNavigateToCsvImport = { navController.navigateToCsvImport() },
            onNavigateToCsvExport = { navController.navigateToCsvExport() },
            onNavigateBack = { navController.popBackStack() }
        )
        contactsImportDestination(onNavigateBack = { navController.popBackStack() })
        csvImportDestination(onNavigateBack = { navController.popBackStack() })
        csvExportDestination(onNavigateBack = { navController.popBackStack() })

        settingsDestination(
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onNavigateToNotificationFilter = { navController.navigateToNotificationFilter() },
            onNavigateBack = { navController.popBackStack() }
        )
        notificationSettingsDestination(onNavigateBack = { navController.popBackStack() })
        notificationFilterDestination(onNavigateBack = { navController.popBackStack() })

        donationDestination(onNavigateBack = { navController.popBackStack() })

        helpDestination(
            onNavigateToNotificationTroubleshoot =
                { navController.navigateToNotificationTroubleshoot() },
            onNavigateBack = { navController.popBackStack() }
        )
        notificationTroubleshootDestination(onNavigateBack = { navController.popBackStack() })

        aboutDestination(
            onNavigateToThirdPartyLibraries = { navController.navigateToThirdPartyLibraries() },
            onNavigateBack = { navController.popBackStack() }
        )
    }
}