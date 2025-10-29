package com.example.jcarlosvelasco.loopmusic.data.remote.api

sealed class SpotifyException(message: String) : Exception(message) {
    class RateLimitException(val retryAfter: Int?) : SpotifyException("Rate limit exceeded")
    class ServerError(val statusCode: Int) : SpotifyException("Server error: $statusCode")
    class ClientError(val statusCode: Int) : SpotifyException("Client error: $statusCode")
    class NetworkError(cause: Throwable) : SpotifyException("Network error: ${cause.message}")
}