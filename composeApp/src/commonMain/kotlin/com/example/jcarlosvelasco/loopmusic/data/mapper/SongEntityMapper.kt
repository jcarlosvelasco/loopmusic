package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongEntity

class SongEntityMapper(
    private val artistEntityMapper: ArtistEntityMapper,
    private val albumEntityMapper: AlbumEntityMapper
) {
    fun mapToSong(entity: SongEntity, artist: ArtistEntity, album: AlbumEntity): Song {
        return Song(
            name = entity.name,
            path = entity.path,
            artist = artistEntityMapper.mapToArtist(artist),
            duration = entity.duration,
            modificationDate = entity.modificationDate,
            album = albumEntityMapper.mapToAlbum(album, artist),
            trackNumber = entity.trackNumber
        )
    }
}