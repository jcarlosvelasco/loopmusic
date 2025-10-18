package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Artist(
    val id: Long,
    val name: String,
    @Transient
    var artwork: ByteArray? = null
)
