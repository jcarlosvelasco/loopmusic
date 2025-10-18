package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface DeleteSongsFromCacheRepoType {
    suspend fun deleteSongsFromCache(paths: List<String>)
}