package com.kuzepa.mydates.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun EmojiDialog(
    onCloseDialog: (selectedEmoji: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AlertDialog(
        title = {
            Text(
                text = "Select emoji to use as icon" // TODO replace with string resource
            )
        },
        text = {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(Shapes.defaultContainerShape)
                    .background(MyDatesColors.containerColor)
                    .padding(all = 16.dp),
                factory = { _ ->
                    EmojiPickerView(context).apply {
                        emojiGridColumns = 6

                        setOnEmojiPickedListener { emojiItem ->
                            onCloseDialog(emojiItem.emoji)
                        }
                    }
                }
            )
        },
        shape = Shapes.defaultDialogShape,
        onDismissRequest = { onCloseDialog(null) },
        confirmButton = {
            TextButton(
                onClick = {
                    onCloseDialog(null)
                }
            ) {
                Text(stringResource(R.string.button_cancel))
            }
        },
        containerColor = MyDatesColors.screenBackground,
        modifier = modifier
    )
}