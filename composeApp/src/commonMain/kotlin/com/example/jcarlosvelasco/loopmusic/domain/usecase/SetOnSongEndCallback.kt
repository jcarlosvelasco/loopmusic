package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetOnSongEndCallbackRepoType

interface SetOnSongEndCallbackType {
    fun execute(callback: () -> Unit)
}

class SetOnSongEndCallback(
    private val repo: SetOnSongEndCallbackRepoType
): SetOnSongEndCallbackType {
    override fun execute(callback: () -> Unit) {
        repo.setOnSongEndCallback(callback)
    }
}