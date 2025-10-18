package com.example.jcarlosvelasco.loopmusic.di.modules

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class AndroidModule {
    @Single
    fun providesExoPlayer(
        context: Context
    ): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}