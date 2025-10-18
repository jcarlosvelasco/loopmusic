package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface UpdateElapsedRepoType {
    fun updateElapsed(positionMs: Long, isPlaying: Boolean)
}