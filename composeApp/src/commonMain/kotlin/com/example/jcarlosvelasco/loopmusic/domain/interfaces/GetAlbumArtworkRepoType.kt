package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface GetAlbumArtworkRepoType {
    suspend fun getAlbumArtwork(artworkHash: String, isExternal: Boolean): ByteArray?
}