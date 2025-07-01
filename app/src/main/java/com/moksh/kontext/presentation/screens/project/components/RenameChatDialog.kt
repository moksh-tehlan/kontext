package com.moksh.kontext.presentation.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun RenameChatDialog(
    isVisible: Boolean,
    chatName: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean = false
) {
//    val focusRequester = remember { FocusRequester() }

    if (isVisible) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            onDismissRequest = onCancel,
            title = {
                Text(
                    text = "Rename Chat",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter a new name for this chat:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    OutlinedTextField(
                        value = chatName,
                        onValueChange = onNameChange,
                        label = { Text("Chat name") },
                        modifier = Modifier
                            .fillMaxWidth()
                        /*.focusRequester(focusRequester)*/,
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    enabled = !isLoading && chatName.isNotBlank()
                ) {
                    if (isLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(4.dp),
                                strokeWidth = 2.dp
                            )
                            Text("Renaming...")
                        }
                    } else {
                        Text("Rename")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel,
                    enabled = !isLoading
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
@Preview
private fun RenameChatDialogPreview() {
    KontextTheme {
        RenameChatDialog(
            isVisible = true,
            chatName = "Sample Chat",
            onNameChange = {},
            onConfirm = {},
            onCancel = {}
        )
    }
}

@Composable
@Preview
private fun RenameChatDialogLoadingPreview() {
    KontextTheme {
        RenameChatDialog(
            isVisible = true,
            chatName = "Sample Chat",
            onNameChange = {},
            onConfirm = {},
            onCancel = {},
            isLoading = true
        )
    }
} 