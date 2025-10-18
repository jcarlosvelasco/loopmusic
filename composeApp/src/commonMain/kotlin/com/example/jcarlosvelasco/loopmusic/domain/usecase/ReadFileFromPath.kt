package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.ReadFileFromPathRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface ReadFileFromPathType {
    suspend fun execute(path: File): Song
}

class ReadFileFromPath(
    private val repo: ReadFileFromPathRepositoryType
): ReadFileFromPathType {
    override suspend fun execute(path: File): Song {
        return repo.readFile(path)
    }
}