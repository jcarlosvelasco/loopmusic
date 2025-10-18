package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState

interface GetPlaybackStateRepoType {
    fun getPlaybackState(): PlaybackState?
}