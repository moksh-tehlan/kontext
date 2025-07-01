package com.moksh.kontext.presentation.screens.project.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.rightArrowIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    chatName: String,
    date: String = "21 June 2025",
    showOptionsMenu: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onRenameClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismissOptionsMenu: () -> Unit = {}
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = chatName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                )
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = rightArrowIcon,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    contentDescription = "Chat Icon",
                )
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = date,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
            )
        }

        // Options menu positioned on the right side
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.TopEnd
        ) {
            ChatOptionsMenu(
                expanded = showOptionsMenu,
                onDismiss = onDismissOptionsMenu,
                onRenameClick = onRenameClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Preview
@Composable
fun ChatItemPreview() {
    KontextTheme {
        ChatItem(
            chatName = "Sample Chat",
            onClick = {}
        )
    }
}

@Preview
@Composable
fun ChatItemWithMenuPreview() {
    KontextTheme {
        ChatItem(
            chatName = "Sample Chat",
            showOptionsMenu = true,
            onClick = {}
        )
    }
} 