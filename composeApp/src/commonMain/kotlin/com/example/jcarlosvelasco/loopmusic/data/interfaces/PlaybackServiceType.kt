package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PlaybackServiceType {
    fun publishNowPlaying(song: Song, positionMs: Long = 0L, isPlaying: Boolean = true)
    fun updateElapsed(positionMs: Long, isPlaying: Boolean)
    fun setPlaying(isPlaying: Boolean)
}