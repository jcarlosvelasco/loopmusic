package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.RenamePlaylistRepoType

interface RenamePlaylistType {
    suspend fun execute(playlistId: Long, playlistName: String)
}

class RenamePlaylist(
    private val repo: RenamePlaylistRepoType
): RenamePlaylistType {
    override suspend fun execute(playlistId: Long, playlistName: String) {
        repo.renamePlaylist(playlistId, playlistName)
    }
}