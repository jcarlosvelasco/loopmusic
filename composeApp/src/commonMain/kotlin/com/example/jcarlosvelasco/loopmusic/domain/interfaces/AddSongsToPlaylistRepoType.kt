package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface AddSongsToPlaylistRepoType {
    suspend fun addSongsToPlaylist(songs: Set<Song>, playlistId: Long)
}