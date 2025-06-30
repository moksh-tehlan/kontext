package com.moksh.kontext.presentation.screens.knowledge_source.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.repository.KnowledgeSourceRepository
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KnowledgeSourceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val knowledgeSourceRepository: KnowledgeSourceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    private val _knowledgeSourceState = MutableStateFlow(
        KnowledgeSourceScreenState(projectId = projectId)
    )
    val knowledgeSourceState = _knowledgeSourceState.asStateFlow()
        .onStart {
            loadKnowledgeSources()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            KnowledgeSourceScreenState(projectId = projectId)
        )

    private val _addContentBottomSheetState = MutableStateFlow(AddContentBottomSheetState())
    val addContentBottomSheetState = _addContentBottomSheetState.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AddContentBottomSheetState()
        )

    private val _webUrlDialogState = MutableStateFlow(WebUrlDialogState())
    val webUrlDialogState = _webUrlDialogState.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            WebUrlDialogState()
        )

    private val _knowledgeSourceEvents = MutableSharedFlow<KnowledgeSourceScreenEvents>()
    val knowledgeSourceEvents = _knowledgeSourceEvents.asSharedFlow()

    fun onAction(action: KnowledgeSourceScreenActions) {
        when (action) {
            is KnowledgeSourceScreenActions.LoadKnowledgeSources -> {
                loadKnowledgeSources()
            }

            is KnowledgeSourceScreenActions.RefreshKnowledgeSources -> {
                loadKnowledgeSources()
            }

            is KnowledgeSourceScreenActions.ShowAddContentBottomSheet -> {
                _addContentBottomSheetState.update {
                    it.copy(isVisible = true, errorMessage = null)
                }
            }

            is KnowledgeSourceScreenActions.HideAddContentBottomSheet -> {
                _addContentBottomSheetState.update {
                    it.copy(isVisible = false, errorMessage = null)
                }
            }

            is KnowledgeSourceScreenActions.UploadFromDevice -> {
                viewModelScope.launch {
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.OpenFilePicker)
                }
            }

            is KnowledgeSourceScreenActions.UploadFile -> {
                uploadFile(action.file)
            }

            is KnowledgeSourceScreenActions.ShowWebUrlDialog -> {
                _webUrlDialogState.update {
                    it.copy(isVisible = true, url = "", errorMessage = null)
                }
            }

            is KnowledgeSourceScreenActions.HideWebUrlDialog -> {
                _webUrlDialogState.update {
                    it.copy(isVisible = false, url = "", errorMessage = null)
                }
            }

            is KnowledgeSourceScreenActions.UrlChange -> {
                _webUrlDialogState.update {
                    it.copy(url = action.url, errorMessage = null)
                }
            }

            is KnowledgeSourceScreenActions.AddWebUrl -> {
                addWebUrl()
            }

            is KnowledgeSourceScreenActions.DeleteKnowledgeSource -> {
                deleteKnowledgeSource(action.sourceId)
            }
        }
    }

    private fun loadKnowledgeSources() {
        viewModelScope.launch {
            _knowledgeSourceState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = knowledgeSourceRepository.getProjectKnowledgeSources(projectId)) {
                is Result.Success -> {
                    _knowledgeSourceState.update {
                        it.copy(
                            isLoading = false,
                            knowledgeSources = result.data
                        )
                    }
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.KnowledgeSourcesLoadedSuccessfully)
                }

                is Result.Error -> {
                    _knowledgeSourceState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.asUiText().asString(context)
                        )
                    }
                    _knowledgeSourceEvents.emit(
                        KnowledgeSourceScreenEvents.ShowError(
                            result.error.asUiText().asString(context)
                        )
                    )
                }
            }
        }
    }

    private fun addWebUrl() {
        viewModelScope.launch {
            val url = _webUrlDialogState.value.url.trim()

            if (url.isBlank()) {
                _webUrlDialogState.update {
                    it.copy(errorMessage = "Please enter a valid URL")
                }
                return@launch
            }

            if (!isValidUrl(url)) {
                _webUrlDialogState.update {
                    it.copy(errorMessage = "Please enter a valid URL")
                }
                return@launch
            }

            _webUrlDialogState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = knowledgeSourceRepository.createWebKnowledgeSource(projectId, url)) {
                is Result.Success -> {
                    _webUrlDialogState.update {
                        it.copy(isLoading = false, url = "")
                    }
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.KnowledgeSourceAddedSuccessfully)
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.CloseWebUrlDialog)
                    loadKnowledgeSources() // Refresh the list
                }

                is Result.Error -> {
                    _webUrlDialogState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.asUiText().asString(context)
                        )
                    }
                }
            }
        }
    }

    private fun uploadFile(file: File) {
        viewModelScope.launch {
            _knowledgeSourceState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = knowledgeSourceRepository.uploadFile(projectId, file)) {
                is Result.Success -> {
                    _knowledgeSourceState.update {
                        it.copy(isLoading = false)
                    }
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.KnowledgeSourceAddedSuccessfully)
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.CloseAddContentBottomSheet)
                    loadKnowledgeSources() // Refresh the list
                }

                is Result.Error -> {
                    _knowledgeSourceState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.asUiText().asString(context)
                        )
                    }
                    _knowledgeSourceEvents.emit(
                        KnowledgeSourceScreenEvents.ShowError(
                            result.error.asUiText().asString(context)
                        )
                    )
                }
            }
        }
    }

    private fun deleteKnowledgeSource(sourceId: String) {
        viewModelScope.launch {
            when (val result =
                knowledgeSourceRepository.deleteKnowledgeSource(projectId, sourceId)) {
                is Result.Success -> {
                    _knowledgeSourceEvents.emit(KnowledgeSourceScreenEvents.KnowledgeSourceDeletedSuccessfully)
                    loadKnowledgeSources() // Refresh the list
                }

                is Result.Error -> {
                    _knowledgeSourceEvents.emit(
                        KnowledgeSourceScreenEvents.ShowError(
                            result.error.asUiText().asString(context)
                        )
                    )
                }
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val patterns = listOf(
                "^https?://.*".toRegex(),
                "^www\\..*".toRegex()
            )
            patterns.any { it.matches(url) }
        } catch (e: Exception) {
            false
        }
    }

    private fun extractDomainFromUrl(url: String): String {
        return try {
            val cleanUrl = if (!url.startsWith("http")) "https://$url" else url
            val domain = cleanUrl.substringAfter("://").substringBefore("/").substringBefore("?")
            domain.removePrefix("www.")
        } catch (e: Exception) {
            "Web URL"
        }
    }
} 