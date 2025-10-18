package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity

class AlbumMapper {
    fun mapToAlbumEntity(album: Album): AlbumEntity {
        return AlbumEntity(
            name = album.name,
            artistId = album.artist.id,
            artworkHash = album.artworkHash,
            albumId = album.id,
            year = album.year
        )
    }
}