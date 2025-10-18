package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity

class ArtistMapper {
    fun mapToArtistEntity(artist: Artist): ArtistEntity {
        return ArtistEntity(
            artistId = artist.id,
            name = artist.name
        )
    }
}