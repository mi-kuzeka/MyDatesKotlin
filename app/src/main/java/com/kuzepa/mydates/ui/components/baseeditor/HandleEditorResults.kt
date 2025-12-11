package com.kuzepa.mydates.ui.components.baseeditor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kuzepa.mydates.domain.usecase.baseeditor.EditorResultEvent
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HandleEditorResults(
    editorResultEventFlow: SharedFlow<EditorResultEvent>,
    onSuccess: (String?) -> Unit,
    onError: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            editorResultEventFlow.collect { event ->
                when (event) {
                    is EditorResultEvent.SaveSuccess -> onSuccess(event.id)
                    is EditorResultEvent.DeleteSuccess -> onSuccess(null)
                    is EditorResultEvent.OperationError -> onError()
                }
            }
        }
    }
}