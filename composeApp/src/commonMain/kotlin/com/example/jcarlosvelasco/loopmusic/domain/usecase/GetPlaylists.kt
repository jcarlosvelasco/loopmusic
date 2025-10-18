package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaylistsRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface GetPlaylistsType {
    suspend fun execute(): List<Playlist>
}

class GetPlaylists(
    private val repo: GetPlaylistsRepoType
): GetPlaylistsType {
    override suspend fun execute(): List<Playlist> {
        return repo.getPlaylists()
    }
}