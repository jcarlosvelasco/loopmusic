package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface RenamePlaylistRepoType {
    suspend fun renamePlaylist(playlistId: Long, playlistName: String)
}