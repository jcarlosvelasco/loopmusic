package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetAlbumArtworkRepoType

interface GetAlbumArtworkType {
    suspend fun execute(artworkHash: String, isExternal: Boolean = false): ByteArray?
}

class GetAlbumArtwork(
    private val repo: GetAlbumArtworkRepoType
): GetAlbumArtworkType {
    override suspend fun execute(artworkHash: String, isExternal: Boolean): ByteArray? {
        return repo.getAlbumArtwork(artworkHash, isExternal)
    }
}