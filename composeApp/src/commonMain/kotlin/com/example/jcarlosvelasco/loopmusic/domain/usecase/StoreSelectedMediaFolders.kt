package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.StoreSelectedMediaFoldersRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface StoreSelectedMediaFoldersType {
    suspend fun execute(folders: List<Folder>, deletePrevious: Boolean = false)
}

class StoreSelectedMediaFolders(
    private val storeSelectedMediaFoldersRepo: StoreSelectedMediaFoldersRepositoryType
): StoreSelectedMediaFoldersType {
    override suspend fun execute(folders: List<Folder>, deletePrevious: Boolean) {
        storeSelectedMediaFoldersRepo.storeSelectedMediaFolders(folders, deletePrevious)
    }
}