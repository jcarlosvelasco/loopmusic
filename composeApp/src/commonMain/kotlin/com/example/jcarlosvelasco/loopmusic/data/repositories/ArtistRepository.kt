package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.mapper.ArtistResponseMapper
import com.example.jcarlosvelasco.loopmusic.data.remote.api.SpotifyApiService
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheArtistArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetArtistArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCachedArtistArtworkRepoType

class ArtistRepository(
    private val apiService: SpotifyApiService,
    private val artistResponseMapper: ArtistResponseMapper,
    private val files: FilesInfrType
):
    GetArtistArtworkRepoType,
    CacheArtistArtworkRepoType,
    GetCachedArtistArtworkRepoType
{
    override suspend fun getArtistArtwork(artistName: String): ByteArray? {
        val dto = apiService.searchArtist(artistName)
        return artistResponseMapper.mapToImage(dto)
    }

    override suspend fun cacheArtistArtwork(artistName: String, image: ByteArray) {
        files.storeArtistArtwork(image, artistName)
    }

    override suspend fun getCachedArtistArtwork(identifier: String): ByteArray? {
        return files.readCachedArtworkBytes(identifier = identifier, fromSongs = false, isExternal = false)
    }
}