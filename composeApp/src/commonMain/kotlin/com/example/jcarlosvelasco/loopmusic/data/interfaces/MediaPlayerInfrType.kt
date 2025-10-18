package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState

interface MediaPlayerInfrType {
    fun playSong(song: Song)
    fun pauseSong()
    fun resumeSong()
    fun seekTo(progress: Float)
    fun getCurrentPosition(): Float
    fun getCurrentState(): MediaState
    fun putSongInPlayer(song: Song, pos: Float)
    fun setOnSongEndCallback(callback: () -> Unit)
    fun removeOnSongEndCallback()
}