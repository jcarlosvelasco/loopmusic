package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetArtistArtworkRepoType

interface GetArtistArtworkType {
    suspend fun execute(artistName: String): Result<ByteArray?>
}

class GetArtistArtwork(
    private val repo: GetArtistArtworkRepoType
): GetArtistArtworkType {
    override suspend fun execute(artistName: String): Result<ByteArray?> {
        return repo.getArtistArtwork(artistName)
    }
}