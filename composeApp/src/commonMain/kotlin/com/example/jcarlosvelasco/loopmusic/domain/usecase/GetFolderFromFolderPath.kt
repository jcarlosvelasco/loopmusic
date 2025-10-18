package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFolderFromFolderPathRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface GetFolderFromFolderPathType {
    suspend fun execute(folderPath: String): Folder?
}

class GetFolderFromFolderPath(
    private val repo: GetFolderFromFolderPathRepoType
): GetFolderFromFolderPathType {
    override suspend fun execute(folderPath: String): Folder? {
        return repo.getFolderFromFolderPath(folderPath)
    }
}