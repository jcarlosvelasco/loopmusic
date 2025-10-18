package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface GetCachedArtistArtworkRepoType {
    suspend fun getCachedArtistArtwork(identifier: String): ByteArray?
}