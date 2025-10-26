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
    suspend fun loadArtistArtwork(artists: List<Artist>)
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

    override suspend fun loadArtistArtwork(artists: List<Artist>) {
        coroutineScope {
            val dispatcher = Dispatchers.IO.limitedParallelism(4)
            artists.filter { it.artwork == null }.map { artist ->
                async(dispatcher) {
                    val cached = getCachedArtistArtwork.execute(artist.name)
                    val art = cached ?: getArtistArtwork.execute(artist.name)?.also {
                        cacheArtistArtwork.execute(artist.name, it)
                    }
                    artist.artwork = art
                }
            }.awaitAll()
        }
    }

    override suspend fun cleanIfNeeded() {
        if (Random.nextInt(100) < 5) {
            withContext(Dispatchers.IO) { cleanUnusedArtwork.execute() }
        }
    }
}
