package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.BuildFolderTreeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

interface BuildFolderTreeType {
    fun execute(path: String): Folder
}

class BuildFolderTree(
    private val repo: BuildFolderTreeRepositoryType
): BuildFolderTreeType {
    override fun execute(path: String): Folder {
        return repo.buildFolderTree(path)
    }
}