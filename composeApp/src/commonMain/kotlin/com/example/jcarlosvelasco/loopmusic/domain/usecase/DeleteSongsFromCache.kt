package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.DeleteSongsFromCacheRepoType

interface DeleteSongsFromCacheType {
    suspend fun execute(paths: List<String>)
}

class DeleteSongsFromCache(
    private val repo: DeleteSongsFromCacheRepoType
): DeleteSongsFromCacheType {
    override suspend fun execute(paths: List<String>) {
        repo.deleteSongsFromCache(paths)
    }
}