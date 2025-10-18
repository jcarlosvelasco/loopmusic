package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetSelectedMediaFoldersRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState


interface GetSelectedMediaFoldersType {
    suspend fun execute(): List<Folder>
    suspend fun isEmpty(): Boolean
}

class GetSelectedMediaFolders(
    private val getSelectedMediaFoldersRepo: GetSelectedMediaFoldersRepositoryType
): GetSelectedMediaFoldersType {
    override suspend fun execute(): List<Folder> {
        return getSelectedMediaFoldersRepo.getSelectedMediaFolders()
    }

    override suspend fun isEmpty(): Boolean {
        val folders = execute()
        return folders.find { it.selectionState == SelectionState.SELECTED || it.selectionState == SelectionState.PARTIAL } == null
    }
}