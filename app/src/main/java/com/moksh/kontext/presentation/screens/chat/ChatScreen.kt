package com.moksh.kontext.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.chat.components.ChatInputField
import com.moksh.kontext.presentation.screens.chat.components.MessageItem
import com.moksh.kontext.presentation.screens.chat.components.TypingIndicator
import com.moksh.kontext.presentation.screens.chat.viewmodel.ChatScreenActions
import com.moksh.kontext.presentation.screens.chat.viewmodel.ChatScreenEvents
import com.moksh.kontext.presentation.screens.chat.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(flow = viewModel.chatEvents) { event ->
        when (event) {
            is ChatScreenEvents.ShowError -> {
                // TODO: Show snackbar with error message
                // snackbarHostState.showSnackbar(event.message)
            }

            is ChatScreenEvents.MessageSentSuccessfully -> {
                // Handle message sent successfully
            }

            is ChatScreenEvents.MessageLoadedSuccessfully -> {
                // Handle messages loaded successfully
            }
        }
    }

    ChatScreenView(
        onNavigateBack = onNavigateBack,
        chatState = chatState,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenView(
    onNavigateBack: () -> Unit = {},
    chatState: com.moksh.kontext.presentation.screens.chat.viewmodel.ChatScreenState = com.moksh.kontext.presentation.screens.chat.viewmodel.ChatScreenState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (ChatScreenActions) -> Unit = {}
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = chatState.chatName ?: "Chat",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            ChatInputField(
                value = chatState.currentMessage,
                onValueChange = { onAction(ChatScreenActions.MessageInputChange(it)) },
                onSendMessage = {
                    onAction(ChatScreenActions.SendMessage)
                },
                enabled = !chatState.isSendingMessage && chatState.chatId != null
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Show messages when available
            if (chatState.messages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = true
                ) {
                    // Show typing indicator when sending message
                    if (chatState.isSendingMessage) {
                        item {
                            TypingIndicator()
                        }
                    }
                    
                    items(chatState.messages.reversed()) { message ->
                        MessageItem(
                            message = message,
                            userNickname = chatState.userNickname
                        )
                    }
                }
            } else if (!chatState.isLoading) {
                // Show empty state when no messages or typing indicator if sending first message
                if (chatState.isSendingMessage) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true
                    ) {
                        item {
                            TypingIndicator()
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "How can I help you today?",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Show loading indicator when loading
            if (chatState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    KontextTheme {
        ChatScreenView()
    }
}