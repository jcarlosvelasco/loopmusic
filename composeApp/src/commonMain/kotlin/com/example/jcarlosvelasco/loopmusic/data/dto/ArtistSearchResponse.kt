package com.example.jcarlosvelasco.loopmusic.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchResponse(
    val artists: Artists
)