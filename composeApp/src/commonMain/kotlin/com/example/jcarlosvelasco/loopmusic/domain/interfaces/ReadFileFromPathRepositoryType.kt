package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface ReadFileFromPathRepositoryType {
    suspend fun readFile(path: File): Song
}