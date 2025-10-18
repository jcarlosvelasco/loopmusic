package com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val artistId: Long,
    val name: String
)