package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheAlbumArtworkRepoType

interface CacheAlbumArtworkType {
    suspend fun execute(identifier: String, image: ByteArray)
}

class CacheAlbumArtwork(
    private val repo: CacheAlbumArtworkRepoType
): CacheAlbumArtworkType {
    override suspend fun execute(identifier: String, image: ByteArray) {
        repo.cacheAlbumArtwork(identifier, image)
    }
}