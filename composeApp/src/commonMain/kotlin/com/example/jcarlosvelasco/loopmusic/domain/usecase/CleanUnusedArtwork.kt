package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CleanUnusedArtworkRepoType

interface CleanUnusedArtworkType {
    suspend fun execute()
}

class CleanUnusedArtwork(
    private val repo: CleanUnusedArtworkRepoType
) : CleanUnusedArtworkType {
    override suspend fun execute() {
        repo.cleanUnusedArtwork()
    }
}
