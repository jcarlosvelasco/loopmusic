package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.PlaybackServiceType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.extensions.toMediaArtwork
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.Foundation.NSNumber
import platform.MediaPlayer.*

open class PlaybackService(): PlaybackServiceType {

    init {
        log("PlaybackService", "Init")
    }

    @OptIn(ExperimentalForeignApi::class)
    fun configureAudioSessionForBackground() {
        val session = AVAudioSession.sharedInstance()
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setActive(true, error = null)
        log("PlaybackService", "Audio session configured for background")
    }

    override fun publishNowPlaying(song: Song, positionMs: Long, isPlaying: Boolean) {
        val infoCenter = MPNowPlayingInfoCenter.defaultCenter()
        val nowPlaying = mutableMapOf<Any?, Any?>()

        nowPlaying[MPMediaItemPropertyTitle] = song.name
        nowPlaying[MPMediaItemPropertyArtist] = song.artist.name
        nowPlaying[MPMediaItemPropertyPlaybackDuration] = NSNumber(double = song.duration.toDouble() / 1000.0)
        nowPlaying[MPNowPlayingInfoPropertyElapsedPlaybackTime] = NSNumber(double = positionMs.toDouble() / 1000.0)
        nowPlaying[MPNowPlayingInfoPropertyPlaybackRate] = NSNumber(double = if (isPlaying) 1.0 else 0.0)

        song.album.artwork?.let { byteArray ->
            byteArray.toMediaArtwork().let { artwork ->
                nowPlaying[MPMediaItemPropertyArtwork] = artwork
            }
        }

        infoCenter.nowPlayingInfo = nowPlaying
    }

    //TODO: Implement remote commands and add seek to
    fun registerRemoteCommands(
        onPlay: (() -> Unit)? = null,
        onPause: (() -> Unit)? = null,
        onNext: (() -> Unit)? = null,
        onPrevious: (() -> Unit)? = null
    ) {
        val commandCenter = MPRemoteCommandCenter.sharedCommandCenter()

        commandCenter.playCommand.addTargetWithHandler { _ ->
            onPlay?.invoke()
            MPRemoteCommandHandlerStatusSuccess
        }

        commandCenter.pauseCommand.addTargetWithHandler { _ ->
            onPause?.invoke()
            MPRemoteCommandHandlerStatusSuccess
        }

        onNext?.let {
            commandCenter.nextTrackCommand.enabled = true
            commandCenter.nextTrackCommand.addTargetWithHandler { _ ->
                it()
                MPRemoteCommandHandlerStatusSuccess
            }
        }

        onPrevious?.let {
            commandCenter.previousTrackCommand.enabled = true
            commandCenter.previousTrackCommand.addTargetWithHandler { _ ->
                it()
                MPRemoteCommandHandlerStatusSuccess
            }
        }
    }


    override fun updateElapsed(positionMs: Long, isPlaying: Boolean) {
        val infoCenter = MPNowPlayingInfoCenter.defaultCenter()
        val current = infoCenter.nowPlayingInfo?.toMutableMap() ?: mutableMapOf()
        current[MPNowPlayingInfoPropertyElapsedPlaybackTime] = NSNumber(double = positionMs.toDouble() / 1000.0)
        current[MPNowPlayingInfoPropertyPlaybackRate] = NSNumber(double = if (isPlaying) 1.0 else 0.0)
        infoCenter.nowPlayingInfo = current
    }

    override fun setPlaying(isPlaying: Boolean) {
        val infoCenter = MPNowPlayingInfoCenter.defaultCenter()
        val current = infoCenter.nowPlayingInfo?.toMutableMap() ?: mutableMapOf()
        current[MPNowPlayingInfoPropertyPlaybackRate] = NSNumber(double = if (isPlaying) 1.0 else 0.0)
        infoCenter.nowPlayingInfo = current
    }

    fun seekTo(positionMs: Long) {
        //seekTo.execute(positionMs.toFloat())
        //updateElapsed(positionMs, isPlaying = true)
    }
}