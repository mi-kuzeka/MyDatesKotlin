package com.kuzepa.mydates.ui.components.baseeditor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectDeleting
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import kotlinx.coroutines.flow.Flow

@Composable
fun HandleEditorResults(
    savingFlow: Flow<ObjectSaving>,
    deletingFlow: Flow<ObjectDeleting>,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        savingFlow.collect { event ->
            when (event) {
                is ObjectSaving.Success -> onSuccess()
                is ObjectSaving.Error -> onError()
            }
        }
    }

    LaunchedEffect(key1 = context) {
        deletingFlow.collect { event ->
            when (event) {
                is ObjectDeleting.Success -> onSuccess()
                is ObjectDeleting.Error -> onError()
            }
        }
    }
}