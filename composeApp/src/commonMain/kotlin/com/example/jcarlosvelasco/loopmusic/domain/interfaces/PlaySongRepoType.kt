package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Song

interface PlaySongRepoType {
    fun playSong(song: Song)
}