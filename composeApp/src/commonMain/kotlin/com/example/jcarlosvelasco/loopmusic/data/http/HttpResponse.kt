package com.example.jcarlosvelasco.loopmusic.data.http

data class HttpResponse(
    val statusCode: Int,
    val body: String,
    val isSuccessful: Boolean
)