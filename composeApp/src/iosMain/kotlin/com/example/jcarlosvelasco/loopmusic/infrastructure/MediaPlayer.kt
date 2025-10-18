package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.*
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObjectProtocol

class MediaPlayer(
    private var player: AVPlayer
): MediaPlayerInfrType {

    private var onSongEndCallback: (() -> Unit)? = null
    private var observer: NSObjectProtocol? = null
    private var interruptionObserver: NSObjectProtocol? = null

    init {
        setupAudioSession()
        setupAudioSessionInterruptionHandling()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupAudioSession() {
        try {
            val audioSession = AVAudioSession.sharedInstance()

            val categoryResult = audioSession.setCategory(
                AVAudioSessionCategoryPlayback,
                0u,
                null
            )

            audioSession.setMode(AVAudioSessionModeDefault, null)

            if (!categoryResult) {
                log("AudioSession", "Error setting audio session category")
                return
            }

            val activeResult = audioSession.setActive(true, null)
            if (!activeResult) {
                log("AudioSession", "Error activating audio session")
            } else {
                log("AudioSession", "Audio session configured successfully")
            }
        } catch (e: Exception) {
            log("AudioSession", "Exception configuring audio session: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupAudioSessionInterruptionHandling() {
        interruptionObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVAudioSessionInterruptionNotification,
            `object` = null,
            queue = null
        ) { notification ->
            val userInfo = notification?.userInfo
            val typeValue = userInfo?.get(AVAudioSessionInterruptionTypeKey) as? ULong

            when (typeValue) {
                AVAudioSessionInterruptionTypeBegan -> {
                    log("AudioSession", "Audio session interrupted - pausing playback")
                    player.pause()
                }
                AVAudioSessionInterruptionTypeEnded -> {
                    log("AudioSession", "Audio session interruption ended")
                    val optionsValue = userInfo[AVAudioSessionInterruptionOptionKey] as? ULong
                    if (optionsValue == AVAudioSessionInterruptionOptionShouldResume) {
                        log("AudioSession", "Should resume playback after interruption")

                        val value = activateAudioSession()
                        if (value) player.play()
                    }
                }
            }
        }
    }

    override fun setOnSongEndCallback(callback: () -> Unit) {
        onSongEndCallback = callback
        setupSongEndObserver()
    }

    override fun removeOnSongEndCallback() {
        onSongEndCallback = null
        observer?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            observer = null
        }
    }

    private fun setupSongEndObserver() {
        observer?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
        }

        observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = null,
            queue = null
        ) { _ ->
            onSongEndCallback?.invoke()
        }
    }

    override fun pauseSong() {
        player.pause()
        deactivateAudioSession()
    }

    override fun playSong(song: Song) {
        if (!activateAudioSession()) {
            log("MediaPlayer", "Could not activate audio session")
            return
        }

        val songUri = NSURL(fileURLWithPath = song.path)
        if (player.currentItem == null) {
            log("MediaPlayer", "Current item is null")
            val playerItem = AVPlayerItem(uRL = songUri)
            player = AVPlayer(playerItem)
        } else {
            log("MediaPlayer", "Replacing current item with player item")
            player.replaceCurrentItemWithPlayerItem(AVPlayerItem(uRL = songUri))
        }

        //Is it necessary?
        if (onSongEndCallback != null) {
            setupSongEndObserver()
        }

        player.play()
    }

    override fun resumeSong() {
        if (activateAudioSession()) {
            player.play()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun activateAudioSession(): Boolean {
        val result = AVAudioSession.sharedInstance().setActive(true, null)

        if (!result) {
            log("AudioSession", "Error activating audio session")
            return false
        }

        log("AudioSession", "Audio session activated")
        return true
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun deactivateAudioSession() {
        if (getCurrentState() != MediaState.PLAYING) {
            val result = AVAudioSession.sharedInstance().setActive(false, null)

            if (!result) {
                log("AudioSession", "Error deactivating audio session")
            } else {
                log("AudioSession", "Audio session deactivated")
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun seekTo(progress: Float) {
        val seconds = progress / 1000.0
        val time = CMTimeMakeWithSeconds(seconds, NSEC_PER_SEC.toInt())
        player.seekToTime(time)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun getCurrentPosition(): Float {
        val time = player.currentTime()
        val seconds = CMTimeGetSeconds(time)
        if (seconds.isNaN()) return 0f
        return (seconds * 1000.0).toFloat()
    }

    override fun getCurrentState(): MediaState {
        return when (player.timeControlStatus) {
            AVPlayerTimeControlStatusPlaying -> MediaState.PLAYING
            AVPlayerTimeControlStatusPaused -> MediaState.PAUSED
            else -> MediaState.NONE
        }
    }

    override fun putSongInPlayer(song: Song, pos: Float) {
        val songUri = NSURL(fileURLWithPath = song.path)
        val playerItem = AVPlayerItem(uRL = songUri)
        if (player.currentItem == null) {
            player = AVPlayer(playerItem)
        } else {
            player.replaceCurrentItemWithPlayerItem(playerItem)
        }

        //Is it necessary?
        if (onSongEndCallback != null) {
            setupSongEndObserver()
        }

        seekTo(pos)
    }

    fun cleanup() {
        removeOnSongEndCallback()

        interruptionObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            interruptionObserver = null
        }

        deactivateAudioSession()
    }


    //Helpers to simulate audio interruptions
    /*
    fun simulateAudioInterruptionBegan() {
        val userInfo = mapOf(
            AVAudioSessionInterruptionTypeKey to NSNumber(AVAudioSessionInterruptionTypeBegan)
        )
        NSNotificationCenter.defaultCenter.postNotificationName(
            aName = AVAudioSessionInterruptionNotification,
            `object` = null,
            userInfo = userInfo as NSDictionary
        )
    }

    fun simulateAudioInterruptionEnded(shouldResume: Boolean) {
        val options = if (shouldResume) AVAudioSessionInterruptionOptionShouldResume else 0u
        val userInfo = mapOf(
            AVAudioSessionInterruptionTypeKey to NSNumber(AVAudioSessionInterruptionTypeEnded),
            AVAudioSessionInterruptionOptionKey to NSNumber(options)
        )
        NSNotificationCenter.defaultCenter.postNotificationName(
            aName = AVAudioSessionInterruptionNotification,
            `object` = null,
            userInfo = userInfo as NSDictionary
        )
    }*/
}