package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface GetPlaylistsRepoType {
    suspend fun getPlaylists(): List<Playlist>
}