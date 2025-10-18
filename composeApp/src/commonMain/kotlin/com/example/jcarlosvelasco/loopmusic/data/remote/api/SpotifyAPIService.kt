package com.example.jcarlosvelasco.loopmusic.data.remote.api

import com.example.jcarlosvelasco.loopmusic.data.dto.ArtistSearchResponse
import com.example.jcarlosvelasco.loopmusic.data.dto.TokenResponse
import com.example.jcarlosvelasco.loopmusic.data.http.ContentTypes
import com.example.jcarlosvelasco.loopmusic.data.http.HttpHeaders
import com.example.jcarlosvelasco.loopmusic.data.interfaces.HttpClientType
import com.example.jcarlosvelasco.loopmusic.utils.encodeBase64
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

class SpotifyApiService(
    private val httpClient: HttpClientType,
    private val clientId: String,
    private val clientSecret: String,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    private var accessToken: String? = null
    private var tokenExpirationTime: Long = 0

    private suspend fun getAccessToken(): String {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (accessToken != null && currentTime < tokenExpirationTime) {
            return accessToken!!
        }

        val credentials = "$clientId:$clientSecret"
        val encodedCredentials = credentials.encodeBase64()

        val response = httpClient.post(
            url = "https://accounts.spotify.com/api/token",
            headers = mapOf(
                HttpHeaders.AUTHORIZATION to "Basic $encodedCredentials",
                HttpHeaders.CONTENT_TYPE to ContentTypes.APPLICATION_FORM_URLENCODED
            ),
            body = "grant_type=client_credentials"
        )

        if (!response.isSuccessful) {
            throw Exception("Error getting access token: ${response.statusCode}")
        }

        val tokenResponse = json.decodeFromString<TokenResponse>(response.body)

        accessToken = tokenResponse.access_token
        tokenExpirationTime = Clock.System.now().toEpochMilliseconds() +
                (tokenResponse.expires_in - 60) * 1000

        return accessToken!!
    }

    suspend fun searchArtist(
        artistName: String,
        limit: Int = 1
    ): ArtistSearchResponse {
        val token = getAccessToken()

        val response = httpClient.get(
            url = "https://api.spotify.com/v1/search",
            headers = mapOf(
                HttpHeaders.AUTHORIZATION to "Bearer $token"
            ),
            queryParameters = mapOf(
                "q" to artistName,
                "type" to "artist",
                "limit" to limit.toString()
            )
        )

        if (!response.isSuccessful) {
            throw Exception("Error searching artist: ${response.statusCode}")
        }

        return json.decodeFromString(response.body)
    }
}