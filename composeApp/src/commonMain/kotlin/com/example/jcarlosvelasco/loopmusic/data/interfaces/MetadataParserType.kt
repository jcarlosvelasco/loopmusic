package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.AudioMetadata

interface MetadataParserType {
    suspend fun parseMetadata(path: String): AudioMetadata
    suspend fun getFullQualityArtwork(path: String): ByteArray?
}