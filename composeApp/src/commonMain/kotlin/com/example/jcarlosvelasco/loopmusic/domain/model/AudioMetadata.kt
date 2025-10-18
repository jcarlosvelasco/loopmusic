package com.example.jcarlosvelasco.loopmusic.domain.model

data class AudioMetadata(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val albumArtist: String? = null,
    val genre: String? = null,
    val year: String? = null,
    val date: String? = null,
    val duration: Long? = null,
    val trackNumber: String? = null,
    val discNumber: String? = null,
    val composer: String? = null,
    val writer: String? = null,
    val bitrate: String? = null,
    val mimeType: String? = null,
    val artwork: ByteArray? = null
)