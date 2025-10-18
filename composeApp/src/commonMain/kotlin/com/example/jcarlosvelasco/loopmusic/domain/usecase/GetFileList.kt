package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFilesFromFolderRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState

interface GetFileListType {
    suspend fun execute(folders: List<Folder>): List<File>
}

class GetFileList(
    private val repo: GetFilesFromFolderRepositoryType
) : GetFileListType {
    override suspend fun execute(folders: List<Folder>): List<File> {
        val files = mutableListOf<File>()
        for (folder in folders) {
            if (folder.selectionState == SelectionState.SELECTED || folder.selectionState == SelectionState.PARTIAL) {
                files.addAll(repo.getFilesFromFolder(folder))
                files.addAll(execute(folder.subfolders))
            }
        }
        return files
    }
}
