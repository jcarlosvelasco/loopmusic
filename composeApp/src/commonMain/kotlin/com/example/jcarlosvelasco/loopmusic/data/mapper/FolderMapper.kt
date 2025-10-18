package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity

class FolderMapper {
    fun mapToEntity(folder: Folder): FolderEntity {
        val entity = FolderEntity(
            path = folder.path,
            name = folder.name,
            subfolders = folder.subfolders.map { mapToEntity(it) },
            selectionState = folder.selectionState,
            rootParent = if (folder.rootParent != null) { mapToEntity(folder.rootParent) } else null)
        return entity
    }
}