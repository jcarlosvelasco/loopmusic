package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaybackState(
    val playlist: List<Song> = emptyList(),
    val currentPosition: Float = 0f,
    val currentIndex: Int? = null,
    val playlistName: String
)