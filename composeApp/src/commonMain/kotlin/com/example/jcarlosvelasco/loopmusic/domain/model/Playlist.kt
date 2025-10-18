package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.datetime.Clock

data class Playlist(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var songPaths: MutableList<String> = mutableListOf()
)