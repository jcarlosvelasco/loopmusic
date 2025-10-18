package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.data.http.HttpResponse


interface HttpClientType {
    suspend fun post(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: String? = null
    ): HttpResponse

    suspend fun get(
        url: String,
        headers: Map<String, String> = emptyMap(),
        queryParameters: Map<String, String> = emptyMap()
    ): HttpResponse

    suspend fun getBytes(
        url: String,
        headers: Map<String, String> = emptyMap()
    ): ByteArray
}