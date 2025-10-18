package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetPlayModeRepoType
import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode

interface SetPlayModeType {
    fun execute(mode: PlayMode)
}

class SetPlayMode(
    private val repo: SetPlayModeRepoType
): SetPlayModeType {
    override fun execute(mode: PlayMode) {
        repo.setPlayMode(mode)
    }
}