package com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.PublishNowPlayingRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PublishNowPlayingType {
    fun execute(song: Song, positionMs: Long, isPlaying: Boolean)
}

class PublishNowPlaying(
    private val repo: PublishNowPlayingRepoType
): PublishNowPlayingType {
    override fun execute(song: Song, positionMs: Long, isPlaying: Boolean) {
        repo.publishNowPlaying(song, positionMs, isPlaying)
    }
}