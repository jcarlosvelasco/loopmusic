package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface UpdateFolderSelectionType {
    fun execute(folder: Folder, select: Boolean): Folder
}

class UpdateFolderSelection : UpdateFolderSelectionType {
    override fun execute(folder: Folder, select: Boolean): Folder {
        return updateSelectionRecursively(folder, select)
    }

    private fun updateSelectionRecursively(folder: Folder, isSelected: Boolean): Folder {
        return folder.copy(

            subfolders = folder.subfolders.map { updateSelectionRecursively(it, isSelected) }
        )
    }
}