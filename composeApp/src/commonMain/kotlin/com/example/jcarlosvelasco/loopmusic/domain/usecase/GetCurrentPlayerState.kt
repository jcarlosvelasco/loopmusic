package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCurrentPlayerStateRepoType
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState

interface GetCurrentPlayerStateType {
    fun execute(): MediaState
}

class GetCurrentPlayerState(
    private val repo: GetCurrentPlayerStateRepoType
): GetCurrentPlayerStateType {
    override fun execute(): MediaState {
        return repo.getCurrentPlayerState()
    }
}