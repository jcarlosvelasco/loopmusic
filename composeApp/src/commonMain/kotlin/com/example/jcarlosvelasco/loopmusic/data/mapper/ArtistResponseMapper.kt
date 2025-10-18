package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.data.dto.ArtistSearchResponse
import com.example.jcarlosvelasco.loopmusic.data.interfaces.HttpClientType
import com.example.jcarlosvelasco.loopmusic.utils.log

class ArtistResponseMapper(
    private val client: HttpClientType
) {
    suspend fun mapToImage(response: ArtistSearchResponse): ByteArray? {
        val imageUrl = response.artists.items.firstOrNull()?.images?.firstOrNull()?.url

        return imageUrl?.let {
            try {
                client.getBytes(it)
            } catch (e: Exception) {
                log("ArtistResponseMapper", "Error downloading image: $e")
                null
            }
        }
    }
}