package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType
import com.example.jcarlosvelasco.loopmusic.infrastructure.MediaPlayer
import platform.AVFoundation.AVPlayer

actual class MediaPlayerFactory {
    actual fun getPlayer(): MediaPlayerInfrType {
        val player = AVPlayer()
        return MediaPlayer(player)
    }
}