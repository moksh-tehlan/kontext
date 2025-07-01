package com.moksh.kontext.presentation.screens.project.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun ChatOptionsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onRenameClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = 0.dp, y = (-12).dp),
        modifier = Modifier.fillMaxWidth(fraction = 0.5f),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        // Rename
        DropdownMenuItem(
            text = {
                Text(
                    text = "Rename",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onRenameClick()
                onDismiss()
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Rename Chat",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        // Delete
        DropdownMenuItem(
            text = {
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onDeleteClick()
                onDismiss()
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete Chat",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
@Preview
private fun ChatOptionsMenuPreview() {
    KontextTheme {
        ChatOptionsMenu(
            expanded = true,
            onDismiss = {}
        )
    }
} 