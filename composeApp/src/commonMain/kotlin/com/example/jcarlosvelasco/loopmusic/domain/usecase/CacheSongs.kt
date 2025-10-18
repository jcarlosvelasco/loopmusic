package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheSongsRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface CacheSongsType {
    suspend fun execute(list: List<Song>, artworkList: List<ByteArray?>)
}

class CacheSongs(
    private val repo: CacheSongsRepositoryType
): CacheSongsType {
    override suspend fun execute(list: List<Song>, artworkList: List<ByteArray?>) {
        repo.cacheSongs(list, artworkList)
    }
}