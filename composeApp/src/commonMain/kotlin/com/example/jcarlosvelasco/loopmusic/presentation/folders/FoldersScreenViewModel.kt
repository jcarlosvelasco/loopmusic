package com.example.jcarlosvelasco.loopmusic.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetSelectedMediaFoldersType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoldersScreenViewModel(
    private val getSelectedMediaFolders: GetSelectedMediaFoldersType
): ViewModel() {
    private val _folders = MutableStateFlow<List<Folder>?>(null)
    val folders = _folders.asStateFlow()

    init {
        loadFolders()
    }

    fun loadFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            val folders = getSelectedMediaFolders.execute()
            _folders.value = getSelectedFolders(folders)
        }
    }

    fun getSelectedFolders(flatFolders: List<Folder>): List<Folder> {
        val result = mutableListOf<Folder>()

        fun collectSelected(folder: Folder) {
            if (folder.selectionState == SelectionState.SELECTED) {
                result.add(
                    folder.copy(
                        subfolders = emptyList()
                    )
                )
            }

            folder.subfolders.forEach { collectSelected(it) }
        }

        flatFolders.forEach { collectSelected(it) }

        return result
    }
}