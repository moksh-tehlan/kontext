package com.moksh.kontext.presentation.screens.knowledge_source

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.domain.model.KnowledgeSourceDto
import com.moksh.kontext.domain.model.KnowledgeSourceStatus
import com.moksh.kontext.domain.model.KnowledgeSourceType
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.knowledge_source.components.AddContentBottomSheet
import com.moksh.kontext.presentation.screens.knowledge_source.components.KnowledgeSourceItem
import com.moksh.kontext.presentation.screens.knowledge_source.components.WebUrlDialog
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.AddContentBottomSheetState
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.KnowledgeSourceScreenActions
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.KnowledgeSourceScreenEvents
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.KnowledgeSourceScreenState
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.KnowledgeSourceViewModel
import com.moksh.kontext.presentation.screens.knowledge_source.viewmodel.WebUrlDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeSourceScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: KnowledgeSourceViewModel = hiltViewModel()
) {
    val knowledgeSourceState by viewModel.knowledgeSourceState.collectAsState()
    val addContentBottomSheetState by viewModel.addContentBottomSheetState.collectAsState()
    val webUrlDialogState by viewModel.webUrlDialogState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // File picker launcher for PDF and TXT files
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            try {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(selectedUri)

                // Validate file type (PDF or TXT)
                if (mimeType == "application/pdf" || mimeType == "text/plain") {
                    // Convert URI to File and upload
                    contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                        val fileName = getFileName(contentResolver, selectedUri) ?: "uploaded_file"
                        val tempFile = java.io.File(context.cacheDir, fileName)
                        tempFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        viewModel.onAction(KnowledgeSourceScreenActions.UploadFile(tempFile))
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please select a PDF or TXT file")
                    }
                }
            } catch (e: Exception) {
                scope.launch {
                    snackbarHostState.showSnackbar("Error reading file: ${e.message}")
                }
            }
        }
    }

    ObserveAsEvents(flow = viewModel.knowledgeSourceEvents) { event ->
        when (event) {
            is KnowledgeSourceScreenEvents.ShowError -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is KnowledgeSourceScreenEvents.KnowledgeSourcesLoadedSuccessfully -> {
                // Handle successful load if needed
            }

            is KnowledgeSourceScreenEvents.KnowledgeSourceAddedSuccessfully -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Knowledge source added successfully!")
                }
            }

            is KnowledgeSourceScreenEvents.KnowledgeSourceDeletedSuccessfully -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Knowledge source deleted successfully!")
                }
            }

            is KnowledgeSourceScreenEvents.CloseAddContentBottomSheet -> {
                // No longer needed - bottom sheet hides immediately when file is picked
            }

            is KnowledgeSourceScreenEvents.CloseWebUrlDialog -> {
                viewModel.onAction(KnowledgeSourceScreenActions.HideWebUrlDialog)
            }

            is KnowledgeSourceScreenEvents.OpenFilePicker -> {
                // Launch file picker with PDF and TXT filter
                filePickerLauncher.launch("*/*")
            }
        }
    }

    KnowledgeSourceScreenView(
        knowledgeSourceState = knowledgeSourceState,
        addContentBottomSheetState = addContentBottomSheetState,
        webUrlDialogState = webUrlDialogState,
        snackbarHostState = snackbarHostState,
        bottomSheetState = bottomSheetState,
        scope = scope,
        onNavigateBack = onNavigateBack,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeSourceScreenView(
    knowledgeSourceState: KnowledgeSourceScreenState = KnowledgeSourceScreenState(),
    addContentBottomSheetState: AddContentBottomSheetState = AddContentBottomSheetState(),
    webUrlDialogState: WebUrlDialogState = WebUrlDialogState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    bottomSheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    scope: CoroutineScope = rememberCoroutineScope(),
    onNavigateBack: () -> Unit = {},
    onAction: (KnowledgeSourceScreenActions) -> Unit = {}
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
                        text = knowledgeSourceState.projectName,
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
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            KontextButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                textColor = MaterialTheme.colorScheme.surface,
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                text = "Add Content",
                isLoading = addContentBottomSheetState.isLoading,
                onClick = {
                    onAction(KnowledgeSourceScreenActions.ShowAddContentBottomSheet)
                },
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (knowledgeSourceState.knowledgeSources.isEmpty() && !knowledgeSourceState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Add relevant documents, text, code, or other files here so Claude can use them as context for all your chats within JAVA DSA Expert.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(knowledgeSourceState.knowledgeSources) { knowledgeSource ->
                        KnowledgeSourceItem(
                            knowledgeSource = knowledgeSource,
                            onDelete = {
                                onAction(
                                    KnowledgeSourceScreenActions.DeleteKnowledgeSource(
                                        knowledgeSource.id
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // Loading indicator (only for initial loading)
            if (knowledgeSourceState.isLoading && knowledgeSourceState.knowledgeSources.isEmpty()) {
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
            knowledgeSourceState.errorMessage?.let { error ->
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

    // Add Content Bottom Sheet
    if (addContentBottomSheetState.isVisible) {
        AddContentBottomSheet(
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide().apply {
                        onAction(KnowledgeSourceScreenActions.HideAddContentBottomSheet)
                    }
                }
            },
            sheetState = bottomSheetState,
            onUploadFromDevice = { onAction(KnowledgeSourceScreenActions.UploadFromDevice) },
            onAddWebUrl = { onAction(KnowledgeSourceScreenActions.ShowWebUrlDialog) }
        )
    }

    // Web URL Dialog
    if (webUrlDialogState.isVisible) {
        WebUrlDialog(
            url = webUrlDialogState.url,
            isLoading = webUrlDialogState.isLoading,
            errorMessage = webUrlDialogState.errorMessage,
            onUrlChange = { onAction(KnowledgeSourceScreenActions.UrlChange(it)) },
            onDismiss = { onAction(KnowledgeSourceScreenActions.HideWebUrlDialog) },
            onAdd = { onAction(KnowledgeSourceScreenActions.AddWebUrl) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun KnowledgeSourceScreenViewPreview() {
    KontextTheme {
        KnowledgeSourceScreenView(
            knowledgeSourceState = KnowledgeSourceScreenState(
                knowledgeSources = listOf(
                    KnowledgeSourceDto(
                        id = "1",
                        name = "research_publication_on_...",
                        type = KnowledgeSourceType.DOCUMENT,
                        createdAt = "2025-05-30T12:00:00Z",
                        updatedAt = "2025-05-30T12:00:00Z",
                        status = KnowledgeSourceStatus.SUCCESS
                    ),
                    KnowledgeSourceDto(
                        id = "2",
                        name = "priority.md",
                        type = KnowledgeSourceType.DOCUMENT,
                        createdAt = "2025-05-30T12:00:00Z",
                        updatedAt = "2025-05-30T12:00:00Z",
                        status = KnowledgeSourceStatus.SUCCESS
                    ),
                    KnowledgeSourceDto(
                        id = "3",
                        name = "kaleshi_hld.md",
                        type = KnowledgeSourceType.DOCUMENT,
                        createdAt = "2025-05-30T12:00:00Z",
                        updatedAt = "2025-05-30T12:00:00Z",
                        status = KnowledgeSourceStatus.SUCCESS
                    )
                )
            )
        )
    }
}

// Helper function to get file name from URI
private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var fileName: String? = null
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName
} 