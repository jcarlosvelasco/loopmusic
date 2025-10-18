package com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetPlayingRepoType

interface SetPlayingType {
    fun execute(isPlaying: Boolean)
}

class SetPlaying(
    private val repo: SetPlayingRepoType
): SetPlayingType {
    override fun execute(isPlaying: Boolean) {
        repo.setPlaying(isPlaying)
    }
}