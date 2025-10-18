package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState

interface SavePlaybackStateRepoType {
    fun savePlaybackState(state: PlaybackState)
    fun getPlaybackState(): PlaybackState?
}