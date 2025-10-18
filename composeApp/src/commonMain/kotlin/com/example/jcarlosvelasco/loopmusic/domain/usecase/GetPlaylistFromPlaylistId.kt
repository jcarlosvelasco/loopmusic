package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaylistFromPlaylistIdRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface GetPlaylistFromPlaylistIdType {
    suspend fun execute(id: Long): Playlist?
}

class GetPlaylistFromPlaylistId(
    private val repo: GetPlaylistFromPlaylistIdRepoType
): GetPlaylistFromPlaylistIdType {
    override suspend fun execute(id: Long): Playlist? {
        return repo.getPlaylistFromPlaylistId(id)
    }
}