package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val path: String,
    val name: String,
    val artist: Artist,
    val modificationDate: Long,
    val album: Album,
    val duration: Long,
    val trackNumber: Int?
)