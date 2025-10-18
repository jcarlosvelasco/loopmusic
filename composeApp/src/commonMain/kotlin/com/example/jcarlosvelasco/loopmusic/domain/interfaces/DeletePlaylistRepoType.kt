package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface DeletePlaylistRepoType {
    suspend fun deletePlaylist(playlistId: Long)
}