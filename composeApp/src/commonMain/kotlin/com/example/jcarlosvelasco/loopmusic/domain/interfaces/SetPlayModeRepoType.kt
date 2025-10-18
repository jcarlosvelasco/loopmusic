package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode

interface SetPlayModeRepoType {
    fun setPlayMode(mode: PlayMode)
}