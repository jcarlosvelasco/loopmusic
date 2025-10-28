package com.example.jcarlosvelasco.loopmusic.presentation.mediaFolders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.domain.usecase.BuildFolderTreeType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetSelectedMediaFoldersType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GivePermissionsType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.OpenDirectoryPickerType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.StoreSelectedMediaFoldersType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaFoldersScreenViewModel(
    getSelectedMediaFolders: GetSelectedMediaFoldersType,
    private val openFilePicker: OpenDirectoryPickerType,
    private val storeSelectedMediaFolders: StoreSelectedMediaFoldersType,
    private val givePermissions: GivePermissionsType,
    private val buildFolderTree: BuildFolderTreeType,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _mediaFolders = MutableStateFlow<List<Folder>?>(null)
    val mediaFolders = _mediaFolders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val fromSettings: Boolean = savedStateHandle.get<Boolean>("fromSettings") ?: false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _mediaFolders.value = getSelectedMediaFolders.execute()
        }
    }

    fun addFolder(folder: Folder) {
        if (_mediaFolders.value?.find { it.path == folder.path } == null) {
            _mediaFolders.update { currentList ->
                currentList?.plus(folder)
            }
        }
    }

    fun openDirectory() {
        viewModelScope.launch {
            val folder = openFilePicker.execute()
            if (folder == null || _mediaFolders.value!!.find { it.path == folder.path } != null) {
                return@launch
            }

            _mediaFolders.value = _mediaFolders.value?.plus(folder)
        }
    }

    fun toggleFolderSelection(folder: Folder) {
        _mediaFolders.update { currentList ->
            currentList?.map { rootFolder ->
                updateFolderSelection(rootFolder, folder)
            }
        }
    }

    private fun updateFolderSelection(currentFolder: Folder, targetFolder: Folder): Folder {
        if (currentFolder.path == targetFolder.path) {
            val newSelectionState = when (currentFolder.selectionState) {
                SelectionState.SELECTED, SelectionState.PARTIAL -> SelectionState.UNSELECTED
                SelectionState.UNSELECTED -> SelectionState.SELECTED
            }

            // Hybrid behavior:
            // - If we select a parent folder (UNSELECTED -> SELECTED): propagate to children
            // - If we deselect any folder: propagate to children
            // - If we change from PARTIAL to SELECTED: propagate to children
            val shouldPropagateToChildren = when {
                currentFolder.selectionState == SelectionState.UNSELECTED && newSelectionState == SelectionState.SELECTED -> true
                currentFolder.selectionState == SelectionState.PARTIAL && newSelectionState == SelectionState.SELECTED -> true
                newSelectionState == SelectionState.UNSELECTED -> true
                else -> false
            }

            val updatedSubfolders = if (shouldPropagateToChildren && currentFolder.subfolders.isNotEmpty()) {
                currentFolder.subfolders.map { subfolder ->
                    propagateSelectionToChildren(subfolder, newSelectionState)
                }
            } else {
                currentFolder.subfolders
            }

            return currentFolder.copy(
                selectionState = newSelectionState,
                subfolders = updatedSubfolders
            )
        }

        if (!isTargetInSubtree(currentFolder, targetFolder)) {
            return currentFolder
        }

        val updatedSubfolders = currentFolder.subfolders.map { subfolder ->
            updateFolderSelection(subfolder, targetFolder)
        }

        val newSelectionState = calculateParentSelectionState(updatedSubfolders)

        return currentFolder.copy(
            selectionState = newSelectionState,
            subfolders = updatedSubfolders
        )
    }

    private fun isTargetInSubtree(currentFolder: Folder, targetFolder: Folder): Boolean {
        return currentFolder.subfolders.any { subfolder ->
            subfolder.path == targetFolder.path || isTargetInSubtree(subfolder, targetFolder)
        }
    }

    private fun propagateSelectionToChildren(folder: Folder, selectionState: SelectionState): Folder {
        return folder.copy(
            selectionState = selectionState,
            subfolders = folder.subfolders.map { subfolder ->
                propagateSelectionToChildren(subfolder, selectionState)
            }
        )
    }

    private fun calculateParentSelectionState(subfolders: List<Folder>): SelectionState {
        if (subfolders.isEmpty()) return SelectionState.UNSELECTED

        val selectedCount = subfolders.count { it.selectionState == SelectionState.SELECTED }
        val partialCount = subfolders.count { it.selectionState == SelectionState.PARTIAL }
        val totalCount = subfolders.size

        return when (selectedCount) {
            totalCount -> SelectionState.SELECTED
            0 if partialCount == 0 -> SelectionState.UNSELECTED
            else -> SelectionState.PARTIAL
        }
    }

    fun removeFolder(folder: Folder) {
        _mediaFolders.update { currentList ->
            currentList?.mapNotNull { rootFolder ->
                removeFolderFromTree(rootFolder, folder)
            }
        }
    }

    private fun removeFolderFromTree(currentFolder: Folder, targetFolder: Folder): Folder? {
        if (currentFolder.path == targetFolder.path) {
            return null
        }

        val updatedSubfolders = currentFolder.subfolders.mapNotNull { subfolder ->
            removeFolderFromTree(subfolder, targetFolder)
        }

        return currentFolder.copy(subfolders = updatedSubfolders)
    }

    fun toggleFolderExpansion(folder: Folder) {
        _mediaFolders.update { currentList ->
            currentList?.map { rootFolder ->
                updateFolderExpansion(rootFolder, folder)
            }
        }
    }

    private fun updateFolderExpansion(currentFolder: Folder, targetFolder: Folder): Folder {
        if (currentFolder.path == targetFolder.path) {
            return currentFolder.copy(isExpanded = !currentFolder.isExpanded)
        }

        val updatedSubfolders = currentFolder.subfolders.map { subfolder ->
            updateFolderExpansion(subfolder, targetFolder)
        }

        return currentFolder.copy(subfolders = updatedSubfolders)
    }

    suspend fun storeFolders() {
        withContext(Dispatchers.IO) {
            storeSelectedMediaFolders.execute(_mediaFolders.value ?: emptyList(), deletePrevious = true)
            for (folder in _mediaFolders.value ?: emptyList()) {
                givePermissions.execute(folder.path)
            }
        }
        log("MediaFoldersScreenViewModel", "Folders stored")
    }

    fun addFolderFromLauncher(path: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val folder = withContext(Dispatchers.IO) {
                    buildFolderTree.execute(path)
                }

                addFolder(folder)
            } finally {
                _isLoading.value = false
            }
        }
    }
}