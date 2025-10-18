package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.PutSongInPlayerRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PutSongInPlayerType {
    fun execute(song: Song, pos: Float)
}

class PutSongInPlayer(
    private val repo: PutSongInPlayerRepoType
): PutSongInPlayerType {
    override fun execute(song: Song, pos: Float) {
        repo.putSongInPlayer(song, pos)
    }
}