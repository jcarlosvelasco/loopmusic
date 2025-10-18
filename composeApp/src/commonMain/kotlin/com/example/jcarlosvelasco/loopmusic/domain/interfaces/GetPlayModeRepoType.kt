package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode

interface GetPlayModeRepoType {
    fun getPlayMode(): PlayMode?
}