package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface StoreSelectedMediaFoldersRepositoryType {
    suspend fun storeSelectedMediaFolders(folders: List<Folder>, deletePrevious: Boolean = false)
}