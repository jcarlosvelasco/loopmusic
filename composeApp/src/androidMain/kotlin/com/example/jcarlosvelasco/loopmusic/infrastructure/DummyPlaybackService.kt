package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.PlaybackServiceType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

class DummyPlaybackService: PlaybackServiceType {
    override fun publishNowPlaying(
        song: Song,
        positionMs: Long,
        isPlaying: Boolean
    ) {

    }

    override fun updateElapsed(positionMs: Long, isPlaying: Boolean) {
    }

    override fun setPlaying(isPlaying: Boolean) {
    }
}