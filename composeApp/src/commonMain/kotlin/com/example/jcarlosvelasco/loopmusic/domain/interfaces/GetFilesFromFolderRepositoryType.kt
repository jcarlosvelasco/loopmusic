package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface GetFilesFromFolderRepositoryType {
    suspend fun getFilesFromFolder(folder: Folder): List<File>
}