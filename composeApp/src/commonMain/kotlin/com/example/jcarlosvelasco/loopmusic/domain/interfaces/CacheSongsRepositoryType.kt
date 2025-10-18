package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface CacheSongsRepositoryType {
    suspend fun cacheSongs(list: List<Song>, artworkList: List<ByteArray?>)
}