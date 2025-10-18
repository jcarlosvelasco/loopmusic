package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistEntity

class PlaylistMapper {
    fun mapToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            name = playlist.name,
            createdAt = playlist.createdAt,
            updatedAt = playlist.updatedAt
        )
    }
}