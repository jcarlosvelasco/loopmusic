package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface GetAlbumArtworkRepoType {
    suspend fun getAlbumArtwork(artworkHash: String): ByteArray?
}