package com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithArtist(
    @Embedded val album: AlbumEntity,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistId"
    )
    val artist: ArtistEntity
)
