package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface GetFolderFromFolderPathRepoType {
    suspend fun getFolderFromFolderPath(path: String): Folder?
}