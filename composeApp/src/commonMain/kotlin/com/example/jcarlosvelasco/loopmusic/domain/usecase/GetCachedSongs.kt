package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCachedSongsRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface GetCachedSongsType {
    suspend fun execute(): List<Song>
}

class GetCachedSongs(
    private val repo: GetCachedSongsRepositoryType
): GetCachedSongsType {
    override suspend fun execute(): List<Song> {
        return repo.getCachedSongs()
    }
}