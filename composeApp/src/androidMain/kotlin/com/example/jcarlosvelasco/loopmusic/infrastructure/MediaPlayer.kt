package com.example.jcarlosvelasco.loopmusic.infrastructure

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.io.IOException

class MediaPlayer(
    private val player: ExoPlayer,
    private val context: Context
): MediaPlayerInfrType {

    private var onSongEndCallback: (() -> Unit)? = null

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var audioFocusRequest: AudioFocusRequest? = null
    private var hasAudioFocus = false
    private var playbackDelayed = false
    private var playbackNowAuthorized = false

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    log("Media player", "Song ended, invoking callback")
                    onSongEndCallback?.invoke()
                    abandonAudioFocus()
                }
            }
        })
    }

    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                log("MediaPlayer", "Audio focus gained")
                hasAudioFocus = true
                if (playbackDelayed || !playbackNowAuthorized) {
                    log("MediaPlayer", "Resuming playback after gaining focus")
                    resumePlayback()
                } else {
                    player.volume = 1.0f
                }
                playbackDelayed = false
                playbackNowAuthorized = false
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                log("MediaPlayer", "Audio focus lost permanently")
                hasAudioFocus = false
                playbackDelayed = false
                playbackNowAuthorized = false
                pausePlayback()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                log("MediaPlayer", "Audio focus lost temporarily")
                hasAudioFocus = false
                playbackDelayed = false
                playbackNowAuthorized = false
                pausePlayback()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                log("MediaPlayer", "Audio focus lost temporarily, can duck")
                player.volume = 0.2f
            }
        }
    }

    private fun requestAudioFocus(): Boolean {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26+
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(false)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()

            audioManager.requestAudioFocus(audioFocusRequest!!)
        } else {
            // API < 26
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }

        when (result) {
            AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                log("MediaPlayer", "Audio focus request failed")
                hasAudioFocus = false
                return false
            }
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                log("MediaPlayer", "Audio focus granted")
                hasAudioFocus = true
                playbackNowAuthorized = true
                return true
            }
            AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                log("MediaPlayer", "Audio focus request delayed")
                hasAudioFocus = false
                playbackDelayed = true
                return false
            }
            else -> return false
        }
    }

    private fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { request ->
                audioManager.abandonAudioFocusRequest(request)
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }
        hasAudioFocus = false
        log("MediaPlayer", "Audio focus abandoned")
    }


    //TODO: COULD WE MOVE ALL TO PLAYER?
    fun setRepeatMode(listMode: ListMode) {
        when (listMode) {
            ListMode.ONE_SONG -> player.repeatMode = Player.REPEAT_MODE_ONE
            ListMode.LOOP -> player.repeatMode = Player.REPEAT_MODE_ALL
            ListMode.ONE_TIME -> player.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    override fun setOnSongEndCallback(callback: () -> Unit) {
        onSongEndCallback = callback
    }

    override fun removeOnSongEndCallback() {
        onSongEndCallback = null
    }

    private fun resumePlayback() {
        if (!player.isPlaying) {
            player.play()
        }
    }

    private fun pausePlayback() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    override fun playSong(song: Song) {
        log("Media player", "Playing song: ${song.name}")

        if (!requestAudioFocus()) {
            log("MediaPlayer", "Could not get audio focus, playback may be delayed")
            if (!playbackDelayed) {
                return // Do not play if focus cannot be obtained and is not delayed
            }
        }

        try {
            val mediaItem = MediaItem.Builder()
                .setUri(song.path.toUri())
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            if (playbackNowAuthorized || playbackDelayed) {
                player.play()
            }
        } catch (e: IOException) {
            log("Media player", "Could not play song: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun putSongInPlayer(song: Song, pos: Float) {
        log("Media player", "Putting song in player: ${song.name}")
        player.setMediaItem(MediaItem.fromUri(song.path.toUri()))
        player.seekTo(pos.toLong())
        player.prepare()
    }

    override fun pauseSong() {
        player.pause()
    }

    override fun resumeSong() {
        log("Media player", "Resuming song")
        if (player.isPlaying) {
            return
        }

        if (!hasAudioFocus) {
            if (!requestAudioFocus()) {
                log("MediaPlayer", "Could not get audio focus for resume")
                return
            }
        }

        player.play()
    }

    override fun seekTo(progress: Float) {
        log("Media player", "Seeking to $progress")
        player.seekTo(progress.toLong())
    }

    override fun getCurrentPosition(): Float {
        return player.currentPosition.toFloat()
    }

    override fun getCurrentState(): MediaState {
        return if (player.isPlaying) MediaState.PLAYING else MediaState.PAUSED
    }

    fun release() {
        abandonAudioFocus()
        player.release()
    }
}