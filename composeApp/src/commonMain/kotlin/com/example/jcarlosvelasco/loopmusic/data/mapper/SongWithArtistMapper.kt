package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongWithArtistAndAlbum

class SongWithArtistMapper(
    private val artistMapper: ArtistEntityMapper,
    private val albumMapper: AlbumEntityMapper
) {
    fun mapToSong(song: SongWithArtistAndAlbum): Song {
        return Song(
            path = song.song.path,
            name = song.song.name,
            artist = artistMapper.mapToArtist(song.artist),
            modificationDate = song.song.modificationDate,
            album = albumMapper.mapToAlbum(song.album, song.artist),
            duration = song.song.duration,
            trackNumber = song.song.trackNumber
        )
    }
}