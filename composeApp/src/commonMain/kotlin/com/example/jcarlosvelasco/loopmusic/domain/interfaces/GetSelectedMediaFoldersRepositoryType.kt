package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface GetSelectedMediaFoldersRepositoryType {
    suspend fun getSelectedMediaFolders(): List<Folder>
}