package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongEntity

class SongMapper {
    fun mapToSongEntity(song: Song, artistID: Long, albumID: Long): SongEntity {
        val entity = SongEntity(
            path = song.path,
            modificationDate = song.modificationDate,
            artistId = artistID,
            name = song.name,
            albumId = albumID,
            duration = song.duration,
            trackNumber = song.trackNumber
        )
        return entity
    }
}