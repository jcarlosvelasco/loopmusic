package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.AddSongsToPlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface AddSongsToPlaylistType {
    suspend fun execute(songs: Set<Song>, playlistId: Long)
}


class AddSongsToPlaylist(
    private val repo: AddSongsToPlaylistRepoType
): AddSongsToPlaylistType {
    override suspend fun execute(songs: Set<Song>, playlistId: Long) {
        repo.addSongsToPlaylist(songs, playlistId)
    }
}