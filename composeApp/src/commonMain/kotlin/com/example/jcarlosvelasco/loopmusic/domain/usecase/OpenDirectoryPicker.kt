package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.OpenDirectoryPickerRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface OpenDirectoryPickerType {
    suspend fun execute(): Folder?
}

class OpenDirectoryPicker(
    private val openDirectoryPickerRepo: OpenDirectoryPickerRepositoryType
): OpenDirectoryPickerType {
    override suspend fun execute(): Folder? {
        val folder = openDirectoryPickerRepo.openDirectoryPicker()

        /*if (folder != null) {
            folder.isSelected = true
        }*/

        return folder
    }
}