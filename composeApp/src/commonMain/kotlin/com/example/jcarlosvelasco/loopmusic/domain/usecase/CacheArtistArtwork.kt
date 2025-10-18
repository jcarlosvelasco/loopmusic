package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheArtistArtworkRepoType

interface CacheArtistArtworkType {
    suspend fun execute(artistName: String, image: ByteArray)
}

class CacheArtistArtwork(
    private val repo: CacheArtistArtworkRepoType
): CacheArtistArtworkType {
    override suspend fun execute(artistName: String, image: ByteArray) {
        repo.cacheArtistArtwork(artistName, image)
    }
}