package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface GetFullQualityArtworkRepoType {
    suspend fun getFullQualityArtwork(songPath: String): ByteArray?
}