package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFullQualityArtworkRepoType

interface GetFullQualityArtworkType {
    suspend fun execute(songPath: String): ByteArray?
}

class GetFullQualityArtwork(
    private val repo: GetFullQualityArtworkRepoType
): GetFullQualityArtworkType {
    override suspend fun execute(songPath: String): ByteArray? {
        return repo.getFullQualityArtwork(songPath)
    }
}