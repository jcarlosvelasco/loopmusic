package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface BuildFolderTreeRepositoryType {
    fun buildFolderTree(path: String): Folder
}