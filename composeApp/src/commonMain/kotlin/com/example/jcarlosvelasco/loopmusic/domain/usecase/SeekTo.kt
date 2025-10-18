package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SeekToRepoType

interface SeekToType {
    fun execute(progress: Float)
}

class SeekTo(
    private val repo: SeekToRepoType
): SeekToType {
    override fun execute(progress: Float) {
        repo.seekTo(progress)
    }
}