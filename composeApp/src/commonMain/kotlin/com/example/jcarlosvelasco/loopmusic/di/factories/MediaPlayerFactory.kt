package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType

expect class MediaPlayerFactory() {
    fun getPlayer(): MediaPlayerInfrType
}