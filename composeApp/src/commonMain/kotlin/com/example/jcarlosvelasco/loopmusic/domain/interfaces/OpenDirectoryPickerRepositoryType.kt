package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface OpenDirectoryPickerRepositoryType {
    suspend fun openDirectoryPicker(): Folder?
}