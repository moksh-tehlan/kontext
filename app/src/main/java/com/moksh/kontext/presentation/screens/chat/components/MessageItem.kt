package com.moksh.kontext.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moksh.kontext.domain.model.ChatMessageDto
import com.moksh.kontext.domain.model.MessageType

@Composable
fun MessageItem(
    message: ChatMessageDto,
    userNickname: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        when (message.type) {
            MessageType.USER -> {
                UserMessage(
                    message = message,
                    userNickname = userNickname
                )
            }

            MessageType.ASSISTANT -> {
                AssistantMessage(
                    message = message
                )
            }
        }
    }
}

@Composable
private fun UserMessage(
    message: ChatMessageDto,
    userNickname: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // User nickname
        Text(
            text = userNickname,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // User message in a box
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun AssistantMessage(
    message: ChatMessageDto,
    modifier: Modifier = Modifier
) {
    // Assistant message without any background or border
    Text(
        text = message.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}