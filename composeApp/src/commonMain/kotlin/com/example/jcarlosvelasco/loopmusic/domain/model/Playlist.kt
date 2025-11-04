package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlin.time.ExperimentalTime

data class Playlist @OptIn(ExperimentalTime::class) constructor(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    var songPaths: MutableList<String> = mutableListOf()
)