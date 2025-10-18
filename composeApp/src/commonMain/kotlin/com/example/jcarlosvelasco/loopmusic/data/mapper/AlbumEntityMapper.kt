package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity

class AlbumEntityMapper(
    private val artistMapper: ArtistEntityMapper
) {
    fun mapToAlbum(album: AlbumEntity, artist: ArtistEntity): Album {
        return Album(
            id = album.albumId,
            name = album.name,
            artist = artistMapper.mapToArtist(artist),
            artwork = null,
            artworkHash = album.artworkHash,
            year = album.year
        )
    }
}
