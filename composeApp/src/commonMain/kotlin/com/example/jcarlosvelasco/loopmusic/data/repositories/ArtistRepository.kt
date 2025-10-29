package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.mapper.ArtistResponseMapper
import com.example.jcarlosvelasco.loopmusic.data.remote.api.SpotifyApiService
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheArtistArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetArtistArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCachedArtistArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.utils.log

class ArtistRepository(
    private val apiService: SpotifyApiService,
    private val artistResponseMapper: ArtistResponseMapper,
    private val files: FilesInfrType
):
    GetArtistArtworkRepoType,
    CacheArtistArtworkRepoType,
    GetCachedArtistArtworkRepoType
{
    override suspend fun getArtistArtwork(artistName: String): Result<ByteArray?> {
        return try {
            val dto = apiService.searchArtist(artistName)

            dto.fold(
                onSuccess = { response ->
                    Result.success(artistResponseMapper.mapToImage(response))
                },
                onFailure = { exception ->
                    log("ArtistRepo", "Failed to get artwork for $artistName: ${exception.message}")
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            log("ArtistRepo", "Unexpected error getting artwork for $artistName: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun cacheArtistArtwork(artistName: String, image: ByteArray) {
        files.storeArtistArtwork(image, artistName)
    }

    override suspend fun getCachedArtistArtwork(identifier: String): ByteArray? {
        return files.readCachedArtworkBytes(identifier = identifier, fromSongs = false, isExternal = false)
    }
}