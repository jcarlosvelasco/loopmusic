package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity

class FolderEntityMapper {
    fun mapToFolder(entity: FolderEntity): Folder {
        val folder = Folder(
            path = entity.path,
            name = entity.name,
            subfolders = entity.subfolders.map { mapToFolder(it) },
            selectionState = entity.selectionState,
            rootParent = if (entity.rootParent != null) { mapToFolder(entity.rootParent) } else null
        )
        return folder
    }
}