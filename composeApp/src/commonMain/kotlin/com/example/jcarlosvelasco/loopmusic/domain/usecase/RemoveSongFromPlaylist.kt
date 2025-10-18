package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.RemoveSongFromPlaylistRepoType

interface RemoveSongFromPlaylistType {
    suspend fun execute(songPath: String, playlistId: Long)
}

class RemoveSongFromPlaylist(
    private val repo: RemoveSongFromPlaylistRepoType
): RemoveSongFromPlaylistType {
    override suspend fun execute(songPath: String, playlistId: Long) {
        return repo.removeSongFromPlaylist(songPath, playlistId)
    }
}