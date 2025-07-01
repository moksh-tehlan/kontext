package com.moksh.kontext.presentation.screens.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.common.ConfirmationDialog
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.common.deleteIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.DateUtils
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.project.components.ChatItem
import com.moksh.kontext.presentation.screens.project.components.CustomInstruction
import com.moksh.kontext.presentation.screens.project.components.CustomInstructionDialog
import com.moksh.kontext.presentation.screens.project.components.ProjectKnowledge
import com.moksh.kontext.presentation.screens.project.components.RenameChatDialog
import com.moksh.kontext.presentation.screens.project.viewmodel.ProjectScreenActions
import com.moksh.kontext.presentation.screens.project.viewmodel.ProjectScreenEvents
import com.moksh.kontext.presentation.screens.project.viewmodel.ProjectViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToKnowledgeSource: (String) -> Unit = { _ -> },
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val projectState by viewModel.projectState.collectAsState()
    val customInstructionDialogState by viewModel.customInstructionDialogState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ObserveAsEvents(flow = viewModel.projectEvents) { event ->
        when (event) {
            is ProjectScreenEvents.ShowError -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is ProjectScreenEvents.NavigateToChat -> {
                onNavigateToChat(event.projectId, event.chatId)
            }

            is ProjectScreenEvents.ChatsLoadedSuccessfully -> {
                // Handle successful load if needed
            }

            is ProjectScreenEvents.CustomInstructionSavedSuccessfully -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Custom instruction saved successfully!")
                }
            }

            is ProjectScreenEvents.CloseCustomInstructionDialog -> {
                scope.launch {
                    bottomSheetState.hide()
                }
            }

            is ProjectScreenEvents.ChatRenamedSuccessfully -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Chat renamed successfully!")
                }
            }

            is ProjectScreenEvents.ChatDeletedSuccessfully -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Chat deleted successfully!")
                }
            }
        }
    }

    ProjectScreenView(
        projectState = projectState,
        customInstructionDialogState = customInstructionDialogState,
        snackbarHostState = snackbarHostState,
        bottomSheetState = bottomSheetState,
        scope = scope,
        onNavigateBack = onNavigateBack,
        onNavigateToKnowledgeSource = onNavigateToKnowledgeSource,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreenView(
    projectState: com.moksh.kontext.presentation.screens.project.viewmodel.ProjectScreenState = com.moksh.kontext.presentation.screens.project.viewmodel.ProjectScreenState(),
    customInstructionDialogState: com.moksh.kontext.presentation.screens.project.viewmodel.CustomInstructionDialogState = com.moksh.kontext.presentation.screens.project.viewmodel.CustomInstructionDialogState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    bottomSheetState: androidx.compose.material3.SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    scope: kotlinx.coroutines.CoroutineScope = rememberCoroutineScope(),
    onNavigateBack: () -> Unit = {},
    onNavigateToKnowledgeSource: (String) -> Unit = { _ -> },
    onAction: (ProjectScreenActions) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = projectState.projectName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onAction(ProjectScreenActions.CreateNewChat) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat"
                    )
                },
                text = {
                    Text("New Chat")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ProjectKnowledge(
                            modifier = Modifier.weight(1f),
                            totalFiles = projectState.knowledgeSourceCount,
                            onClick = {
                                projectState.projectId?.let { projectId ->
                                    onNavigateToKnowledgeSource(projectId)
                                }
                            }
                        )
                        CustomInstruction(
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(ProjectScreenActions.ShowCustomInstructionDialog) }
                        )
                    }
                }

                item {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Recent chats",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )
                }

                if (projectState.chats.isNotEmpty()) {
                    items(projectState.chats) { chat ->
                        ChatItem(
                            chatName = chat.name,
                            date = DateUtils.formatDateString(chat.updatedAt),
                            showOptionsMenu = projectState.showChatOptionsMenu && projectState.selectedChatId == chat.id,
                            onClick = {
                                onAction(ProjectScreenActions.NavigateToChat(chat.id))
                            },
                            onLongClick = {
                                onAction(ProjectScreenActions.ShowChatOptionsMenu(chat.id))
                            },
                            onRenameClick = {
                                onAction(
                                    ProjectScreenActions.ShowRenameChatDialog(
                                        chat.id,
                                        chat.name
                                    )
                                )
                            },
                            onDeleteClick = {
                                onAction(
                                    ProjectScreenActions.ShowDeleteChatDialog(
                                        chat.id,
                                        chat.name
                                    )
                                )
                            },
                            onDismissOptionsMenu = {
                                onAction(ProjectScreenActions.HideChatOptionsMenu)
                            }
                        )
                    }
                }
            }

            // Centered empty state for chats
            if (projectState.chats.isEmpty() && !projectState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 140.dp), // Account for the ProjectKnowledge and CustomInstruction sections
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chat's you've had with Kontext will show up here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }

            // Loading indicator
            if (projectState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Error message
            projectState.errorMessage?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    // Custom Instruction Dialog
    if (customInstructionDialogState.isVisible) {
        CustomInstructionDialog(
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide().apply {
                        onAction(ProjectScreenActions.HideCustomInstructionDialog)
                    }
                }
            },
            sheetState = bottomSheetState,
            isLoading = customInstructionDialogState.isLoading,
            instruction = customInstructionDialogState.instruction,
            onInstructionChange = { onAction(ProjectScreenActions.InstructionChange(it)) },
            onSaveInstruction = { onAction(ProjectScreenActions.SaveCustomInstruction) }
        )
    }

    // Rename Chat Dialog
    RenameChatDialog(
        isVisible = projectState.showRenameChatDialog,
        chatName = projectState.renameChatName,
        onNameChange = { onAction(ProjectScreenActions.RenameChatNameChange(it)) },
        onConfirm = { onAction(ProjectScreenActions.ConfirmRenameChat) },
        onCancel = { onAction(ProjectScreenActions.HideRenameChatDialog) },
        isLoading = projectState.isRenamingChat
    )

    // Delete Chat Confirmation Dialog
    projectState.deleteChatInfo?.let { deleteChatInfo ->
        ConfirmationDialog(
            isVisible = projectState.showDeleteChatDialog,
            title = "Delete Chat",
            message = "Are you sure you want to delete \"${deleteChatInfo.chatName}\"? This action cannot be undone and all messages will be permanently removed.",
            confirmText = "Delete Chat",
            cancelText = "Cancel",
            icon = deleteIcon,
            isDestructive = true,
            isLoading = projectState.isDeletingChat,
            onConfirm = { onAction(ProjectScreenActions.ConfirmDeleteChat(deleteChatInfo.chatId)) },
            onCancel = { onAction(ProjectScreenActions.HideDeleteChatDialog) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ProjectScreenViewPreview() {
    KontextTheme { ProjectScreenView() }
}
