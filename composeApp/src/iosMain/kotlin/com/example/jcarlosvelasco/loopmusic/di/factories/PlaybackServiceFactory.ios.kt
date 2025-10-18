package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.PlaybackServiceType
import com.example.jcarlosvelasco.loopmusic.infrastructure.SharedPlaybackService

actual class PlaybackServiceFactory actual constructor() {
    actual fun createPlaybackService(): PlaybackServiceType {
        return SharedPlaybackService
    }
}