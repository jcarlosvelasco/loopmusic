package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SavePlaybackStateRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState
import com.example.jcarlosvelasco.loopmusic.utils.log

interface SavePlaybackStateType {
    fun execute(state: PlaybackState)
}

class SavePlaybackState(
    private val repo: SavePlaybackStateRepoType
): SavePlaybackStateType {
    override fun execute(state: PlaybackState) {
        log("SavePlaybackState","Executing SavePlaybackState use case")
        repo.savePlaybackState(state)
    }
}