package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Album(
    val id: Long,
    val name: String,
    val artist: Artist,
    @Transient
    val artwork: ByteArray? = null,
    val artworkHash: String?,
    val year: Int?
)
