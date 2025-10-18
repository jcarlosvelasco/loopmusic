package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.PlaySongRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PlaySongType {
    fun execute(song: Song)
}

class PlaySong(
    private val repo: PlaySongRepoType
): PlaySongType {
    override fun execute(song: Song) {
        repo.playSong(song)
    }
}