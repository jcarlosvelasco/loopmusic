package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCachedArtistArtworkRepoType

interface GetCachedArtistArtworkType {
    suspend fun execute(artistIdentifier: String): ByteArray?
}

class GetCachedArtistArtwork(
    private val repo: GetCachedArtistArtworkRepoType
): GetCachedArtistArtworkType {
    override suspend fun execute(artistIdentifier: String): ByteArray? {
        return repo.getCachedArtistArtwork(artistIdentifier)
    }
}