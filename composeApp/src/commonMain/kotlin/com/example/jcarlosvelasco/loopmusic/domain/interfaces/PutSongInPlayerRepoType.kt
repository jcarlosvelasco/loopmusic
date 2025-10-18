package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PutSongInPlayerRepoType {
    fun putSongInPlayer(song: Song, pos: Float)
}