package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.StorePlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface StorePlaylistType {
    suspend fun execute(playlist: Playlist): Long
}

class StorePlaylist(
    private val repo: StorePlaylistRepoType
): StorePlaylistType {
    override suspend fun execute(playlist: Playlist): Long {
        return repo.storePlaylist(playlist)
    }
}