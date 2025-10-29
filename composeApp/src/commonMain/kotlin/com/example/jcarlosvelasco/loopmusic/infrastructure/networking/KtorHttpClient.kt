package com.example.jcarlosvelasco.loopmusic.infrastructure.networking

import com.example.jcarlosvelasco.loopmusic.data.http.HttpResponse
import com.example.jcarlosvelasco.loopmusic.data.interfaces.HttpClientType
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

class KtorHttpClient(): HttpClientType {
    private val ktorClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun post(
        url: String,
        headers: Map<String, String>,
        body: String?
    ): HttpResponse {
        val response: io.ktor.client.statement.HttpResponse = ktorClient.post(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
            body?.let { setBody(it) }
        }

        return HttpResponse(
            statusCode = response.status.value,
            body = response.bodyAsText(),
            isSuccessful = response.status.isSuccess(),
            headers = response.headers.toMap()
        )
    }

    override suspend fun get(
        url: String,
        headers: Map<String, String>,
        queryParameters: Map<String, String>
    ): HttpResponse {
        val response: io.ktor.client.statement.HttpResponse = ktorClient.get(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }

        return HttpResponse(
            statusCode = response.status.value,
            body = response.bodyAsText(),
            isSuccessful = response.status.isSuccess(),
            headers = response.headers.toMap()
        )
    }

    override suspend fun getBytes(
        url: String,
        headers: Map<String, String>
    ): ByteArray {
        return ktorClient.get(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }.body()
    }
}

private fun Headers.toMap(): Map<String, String> {
    return this.entries()
        .associate { (key, values) -> key to values.firstOrNull().orEmpty() }
}