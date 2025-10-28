package com.example.jcarlosvelasco.loopmusic.utils

import com.example.jcarlosvelasco.loopmusic.data.repositories.FilesRepository
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.usecase.CacheAlbumArtworkType
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import platform.Foundation.NSNotificationCenter

class AudioController: KoinComponent {
    private val audioViewModel: AudioViewModel = get()
    private val filesRepository: FilesRepository = get()
    private val scope = CoroutineScope(Dispatchers.Main)

    private val cacheAlbumArtwork: CacheAlbumArtworkType = get()

    init {
        setupNotificationObserver()
    }

    private fun setupNotificationObserver() {
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "PlayExternalAudioFile",
            `object` = null,
            queue = null
        ) { notification ->
            val userInfo = notification?.userInfo
            val fileURL = userInfo?.get("fileURL") as? String

            if (fileURL != null) {
                println("🎵 Recibido archivo para reproducir: $fileURL")
                scope.launch {
                    playFile(fileURL)
                }
            }
        }
    }

    suspend fun playFile(urlString: String) {
        try {
            println("🎵 Intentando reproducir: $urlString")
            val song = filesRepository.readFile(File(urlString, modificationDate = 0L))
            audioViewModel.loadPlaylistAndPlay(listOf(song), isShuffled = false)

            withContext(Dispatchers.IO) {
                let2(song.album.artworkHash, song.album.artwork) { hash, artwork ->
                    log("AudioController", "Caching artwork for ${song.name}")
                    log("AudioController", "artwork size: ${artwork.size}")
                    cacheAlbumArtwork.execute(identifier = hash, image = artwork)
                }
            }
            println("✅ Reproducción iniciada")
        } catch (e: Exception) {
            println("❌ Error reproduciendo archivo: ${e.message}")
            e.printStackTrace()
        }
    }
}