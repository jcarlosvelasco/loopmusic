package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface GetArtistArtworkRepoType {
    suspend fun getArtistArtwork(artistName: String): ByteArray?
}