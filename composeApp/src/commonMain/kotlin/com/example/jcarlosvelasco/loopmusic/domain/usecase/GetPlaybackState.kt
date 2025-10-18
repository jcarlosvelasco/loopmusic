package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaybackStateRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState

interface GetPlaybackStateType {
    fun execute(): PlaybackState?
}

class GetPlaybackState(
    private val repo: GetPlaybackStateRepoType
): GetPlaybackStateType {
    override fun execute(): PlaybackState? {
        return repo.getPlaybackState()
    }
}