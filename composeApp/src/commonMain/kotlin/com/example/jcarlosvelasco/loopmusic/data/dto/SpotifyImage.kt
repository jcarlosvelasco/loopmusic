package com.example.jcarlosvelasco.loopmusic.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyImage(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)