package org.psyche.assistant.Composable.Dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = dismissButtonText)
            }
        }
    )
}
