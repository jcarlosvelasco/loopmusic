package com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "songs")
data class SongEntity (
    @PrimaryKey val path: String,
    val name: String,
    val artistId: Long,
    val modificationDate: Long,
    val albumId: Long,
    val duration: Long,
    val trackNumber: Int?
)
