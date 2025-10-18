package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity

class ArtistEntityMapper {
    fun mapToArtist(entity: ArtistEntity): Artist {
        return Artist(
            id = entity.artistId,
            name = entity.name
        )
    }
}