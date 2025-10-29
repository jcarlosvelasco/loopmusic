package com.example.jcarlosvelasco.loopmusic.presentation.main.manager

import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.usecase.CacheArtistArtworkType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.CleanUnusedArtworkType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetArtistArtworkType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetCachedArtistArtworkType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.*
import kotlin.random.Random

interface ArtworkManagerType {
    suspend fun loadArtistArtwork(artists: List<Artist>): List<Artist>
    suspend fun cleanIfNeeded()
}

class ArtworkManager(
    private val getArtistArtwork: GetArtistArtworkType,
    private val cacheArtistArtwork: CacheArtistArtworkType,
    private val getCachedArtistArtwork: GetCachedArtistArtworkType,
    private val cleanUnusedArtwork: CleanUnusedArtworkType
): ArtworkManagerType {

    init {
        log("ArtworkManager", "Init")
    }

    override suspend fun loadArtistArtwork(artists: List<Artist>): List<Artist> = coroutineScope {
        val dispatcher = Dispatchers.IO.limitedParallelism(2)
        val delayBetweenRequests = 300L

        val (cached, notCached) = artists.partition { artist ->
            getCachedArtistArtwork.execute(artist.name) != null
        }

        log("ArtworkManager", "Found ${cached.size} cached, ${notCached.size} need download")

        val cachedWithArtwork = cached.map { artist ->
            val artwork = getCachedArtistArtwork.execute(artist.name)
            log("ArtworkManager", "Loaded cached artwork for ${artist.name}")
            artist.copy(artwork = artwork)
        }

        val notCachedWithArtwork = notCached.mapIndexed { index, artist ->
            async(dispatcher) {
                delay(index * delayBetweenRequests)

                try {
                    val art = getArtistArtwork.execute(artist.name).getOrNull()
                    art?.let { cacheArtistArtwork.execute(artist.name, it) }
                    log("ArtworkManager", "Downloaded artwork for ${artist.name}")
                    artist.copy(artwork = art)
                } catch (e: Exception) {
                    log("ArtworkManager", "Error loading artwork for ${artist.name}: ${e.message}")
                    artist.copy(artwork = null)
                }
            }
        }.awaitAll()

        val resultMap = (cachedWithArtwork + notCachedWithArtwork).associateBy { it.name }
        artists.map { resultMap[it.name] ?: it }
    }

    override suspend fun cleanIfNeeded() {
        if (Random.nextInt(100) < 5) {
            withContext(Dispatchers.IO) { cleanUnusedArtwork.execute() }
        }
    }
}