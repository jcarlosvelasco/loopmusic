package com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SongWithArtistAndAlbum(
    @Embedded val song: SongEntity,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistId"
    )
    val artist: ArtistEntity,

    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumId"
    )
    val album: AlbumEntity
)
