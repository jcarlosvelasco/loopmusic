package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.mapper.FolderEntityMapper
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFolderFromFolderPathRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.MediaFoldersDao

class FolderRepository(
    private val folderEntityMapper: FolderEntityMapper,
    private val mediaFoldersDao: MediaFoldersDao
): GetFolderFromFolderPathRepoType {

    override suspend fun getFolderFromFolderPath(path: String): Folder? {
        val entity = mediaFoldersDao.getFolderFromPath(path)
        entity?.let {
            return folderEntityMapper.mapToFolder(it)
        }
        return null
    }
}