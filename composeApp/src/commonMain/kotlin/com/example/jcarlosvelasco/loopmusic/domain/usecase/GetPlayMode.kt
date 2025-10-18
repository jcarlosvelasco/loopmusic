package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlayModeRepoType
import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode

interface GetPlayModeType {
    fun execute(): PlayMode?
}

class GetPlayMode(
    private val repo: GetPlayModeRepoType
): GetPlayModeType {
    override fun execute(): PlayMode? {
        return repo.getPlayMode()
    }
}