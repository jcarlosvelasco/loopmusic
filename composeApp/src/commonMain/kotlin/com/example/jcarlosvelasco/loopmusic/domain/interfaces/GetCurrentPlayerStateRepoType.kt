package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState

interface GetCurrentPlayerStateRepoType {
    fun getCurrentPlayerState(): MediaState
}