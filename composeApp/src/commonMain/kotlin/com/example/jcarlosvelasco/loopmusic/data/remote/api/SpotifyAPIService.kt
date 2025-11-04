package com.example.jcarlosvelasco.loopmusic.data.remote.api

import com.example.jcarlosvelasco.loopmusic.data.dto.ArtistSearchResponse
import com.example.jcarlosvelasco.loopmusic.data.dto.TokenResponse
import com.example.jcarlosvelasco.loopmusic.data.http.ContentTypes
import com.example.jcarlosvelasco.loopmusic.data.http.HttpHeaders
import com.example.jcarlosvelasco.loopmusic.data.interfaces.HttpClientType
import com.example.jcarlosvelasco.loopmusic.utils.encodeBase64
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class SpotifyApiService(
    private val httpClient: HttpClientType,
    private val clientId: String,
    private val clientSecret: String,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    private var accessToken: String? = null
    private var tokenExpirationTime: Long = 0

    @OptIn(ExperimentalTime::class)
    private suspend fun getAccessToken(): String {
        val currentTime = kotlin.time.Clock.System.now().toEpochMilliseconds()
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
        tokenExpirationTime = kotlin.time.Clock.System.now().toEpochMilliseconds() +
                (tokenResponse.expires_in - 60) * 1000

        return accessToken!!
    }

    suspend fun searchArtist(
        artistName: String,
        limit: Int = 1,
        maxRetries: Int = 3
    ): Result<ArtistSearchResponse> = withContext(Dispatchers.IO) {
        var lastException: Exception? = null

        repeat(maxRetries) { attempt ->
            try {
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

                when (response.statusCode) {
                    200 -> {
                        val data = json.decodeFromString<ArtistSearchResponse>(response.body)
                        return@withContext Result.success(data)
                    }
                    429 -> {
                        // Rate limit exceeded
                        val retryAfter = response.headers["Retry-After"]?.toIntOrNull() ?: ((attempt + 1) * 2)
                        log("SpotifyAPI", "Rate limit hit for $artistName, waiting ${retryAfter}s")

                        if (attempt < maxRetries - 1) {
                            delay(retryAfter * 1000L)
                            lastException = SpotifyException.RateLimitException(retryAfter)
                        } else {
                            return@withContext Result.failure(SpotifyException.RateLimitException(retryAfter))
                        }
                    }
                    in 500..599 -> {
                        log("SpotifyAPI", "Server error ${response.statusCode} for $artistName")
                        if (attempt < maxRetries - 1) {
                            delay((attempt + 1) * 1000L) // Exponential backoff
                            lastException = SpotifyException.ServerError(response.statusCode)
                        } else {
                            return@withContext Result.failure(SpotifyException.ServerError(response.statusCode))
                        }
                    }
                    in 400..499 -> {
                        log("SpotifyAPI", "Client error ${response.statusCode} for $artistName")
                        return@withContext Result.failure(SpotifyException.ClientError(response.statusCode))
                    }
                    else -> {
                        return@withContext Result.failure(Exception("Unexpected status code: ${response.statusCode}"))
                    }
                }
            } catch (e: Exception) {
                log("SpotifyAPI", "Exception for $artistName: ${e.message}")
                lastException = SpotifyException.NetworkError(e)
                if (attempt < maxRetries - 1) {
                    delay((attempt + 1) * 1000L)
                }
            }
        }

        Result.failure(lastException ?: Exception("Unknown error"))
    }
}