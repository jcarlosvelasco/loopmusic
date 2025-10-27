package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface CacheAlbumArtworkRepoType {
    suspend fun cacheAlbumArtwork(identifier: String, image: ByteArray)
}