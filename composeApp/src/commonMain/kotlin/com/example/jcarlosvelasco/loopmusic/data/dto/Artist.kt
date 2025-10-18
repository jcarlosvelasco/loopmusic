package com.example.jcarlosvelasco.loopmusic.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String,
    val name: String,
    val popularity: Int? = null,
    val genres: List<String> = emptyList(),
    val images: List<SpotifyImage> = emptyList(),
    val followers: Followers? = null,
    val external_urls: ExternalUrls? = null
)