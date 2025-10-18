package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PublishNowPlayingRepoType {
    fun publishNowPlaying(song: Song, positionMs: Long, isPlaying: Boolean)
}