package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.PlaybackServiceType

expect class PlaybackServiceFactory() {
    fun createPlaybackService(): PlaybackServiceType
}
