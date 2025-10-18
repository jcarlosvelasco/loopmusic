package com.example.jcarlosvelasco.loopmusic.domain.interfaces

interface SetOnSongEndCallbackRepoType {
    fun setOnSongEndCallback(callback: () -> Unit)
}