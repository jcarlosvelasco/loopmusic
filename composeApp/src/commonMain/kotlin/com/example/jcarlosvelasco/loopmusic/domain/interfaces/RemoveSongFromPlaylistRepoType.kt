package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface RemoveSongFromPlaylistRepoType {
    suspend fun removeSongFromPlaylist(songPath: String, playlistId: Long)
}