package com.example.jcarlosvelasco.loopmusic

import android.app.Application
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.jcarlosvelasco.loopmusic.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val androidModule = module {
            single<ExoPlayer> {
                ExoPlayer.Builder(get<Context>()).build()
            }
        }

        initKoin(
            config = {
                androidContext(this@MyApp)
            },
            additionalModules = listOf(androidModule)
        )
    }
}