package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface CacheArtistArtworkRepoType {
    suspend fun cacheArtistArtwork(artistName: String, image: ByteArray)
}