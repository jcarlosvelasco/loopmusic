package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistWithSongs

class PlaylistWithSongsMapper() {
    fun mapToPlaylist(
        playlist: PlaylistWithSongs
    ): Playlist {
        return Playlist(
            id = playlist.playlist.id,
            name = playlist.playlist.name,
            createdAt = playlist.playlist.createdAt,
            updatedAt = playlist.playlist.updatedAt,
            songPaths = playlist.songs.map { it.path }.toMutableList()
        )
    }
}