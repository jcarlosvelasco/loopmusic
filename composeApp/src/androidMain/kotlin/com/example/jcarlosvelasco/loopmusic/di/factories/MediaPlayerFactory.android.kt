package com.example.jcarlosvelasco.loopmusic.di.factories

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType
import com.example.jcarlosvelasco.loopmusic.infrastructure.MediaPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class MediaPlayerFactory actual constructor(): KoinComponent  {
    actual fun getPlayer(): MediaPlayerInfrType {
        val player: ExoPlayer by inject()
        val context: Context by inject()
        return MediaPlayer(
            player = player,
            context = context
        )
    }
}