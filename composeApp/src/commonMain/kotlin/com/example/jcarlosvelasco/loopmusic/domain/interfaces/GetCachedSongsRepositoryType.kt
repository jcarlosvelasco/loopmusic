package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface GetCachedSongsRepositoryType {
    suspend fun getCachedSongs(): List<Song>
}