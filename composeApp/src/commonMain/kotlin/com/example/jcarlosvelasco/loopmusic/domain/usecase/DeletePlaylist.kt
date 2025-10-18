package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.DeletePlaylistRepoType

interface DeletePlaylistType {
    suspend fun execute(playlistId: Long)
}

class DeletePlaylist(
    private val repo: DeletePlaylistRepoType
): DeletePlaylistType {
    override suspend fun execute(playlistId: Long) {
        repo.deletePlaylist(playlistId)
    }
}