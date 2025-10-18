package com.example.jcarlosvelasco.loopmusic.infrastructure

import android.app.PendingIntent
import android.content.Intent
import android.media.AudioManager
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.jcarlosvelasco.loopmusic.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaybackService : MediaSessionService(), KoinComponent  {

    val player: ExoPlayer by inject()
    private var mediaSession: MediaSession? = null
    private lateinit var audioManager: AudioManager

    private fun createContentIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    override fun onCreate() {
        super.onCreate()

        val sessionActivityIntent = createContentIntent()

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityIntent)
            .setCallback(MyCallback())
            .build()

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
    }

    private class MyCallback : MediaSession.Callback {
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()

            return MediaSession.ConnectionResult.accept(
                availableSessionCommands.build(),
                connectionResult.availablePlayerCommands
            )
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            //TODO: What if reboot?
            //player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession
}