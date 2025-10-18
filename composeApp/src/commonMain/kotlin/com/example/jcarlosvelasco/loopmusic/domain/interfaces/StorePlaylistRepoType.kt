package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface StorePlaylistRepoType {
    suspend fun storePlaylist(playlist: Playlist): Long
}