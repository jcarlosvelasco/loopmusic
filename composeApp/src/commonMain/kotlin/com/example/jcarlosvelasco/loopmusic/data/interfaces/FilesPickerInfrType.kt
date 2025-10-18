package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface FilesPickerInfrType {
    suspend fun openDirectoryPicker(): Folder?
    fun buildFolderTree(path: String): Folder
}