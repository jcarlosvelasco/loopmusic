package com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.UpdateElapsedRepoType

interface UpdateElapsedType {
    fun execute(positionMs: Long, isPlaying: Boolean)
}

class UpdateElapsed(
    private val repo: UpdateElapsedRepoType
): UpdateElapsedType {
    override fun execute(positionMs: Long, isPlaying: Boolean) {
        repo.updateElapsed(positionMs, isPlaying)
    }
}