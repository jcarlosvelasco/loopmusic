package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.domain.model.AudioMetadata
import com.example.jcarlosvelasco.loopmusic.extensions.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSURL
import kotlin.let

class MetadataParser(
    private val commonMetadataParser: CommonMetadataParser
): MetadataParserType {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun parseMetadata(path: String): AudioMetadata {
        val url = NSURL.fileURLWithPath(path)
        val asset = AVURLAsset.URLAssetWithURL(url, null)

        val metadataMap = mutableMapOf<String?, String>()
        var artworkBytes: ByteArray? = null

        val commonMetadata = asset.commonMetadata
        for (item in commonMetadata) {
            if (item !is AVMetadataItem) continue

            val key = item.commonKey ?: continue
            val value = item.stringValue ?: item.value?.toString()

            if (key == AVMetadataCommonKeyArtwork) {
                val data = item.dataValue
                if (data != null) {
                    artworkBytes = data.toByteArray()
                }
            } else if (value != null) {
                metadataMap[key] = value
            }
        }

        val formatMetadata = asset.metadata
        for (item in formatMetadata) {
            if (item !is AVMetadataItem) continue

            val key = item.key?.toString() ?: continue
            val value = item.stringValue ?: item.value?.toString()

            when (key.lowercase()) {
                "title" -> if (!metadataMap.containsKey(AVMetadataCommonKeyTitle)) {
                    value?.let { metadataMap[AVMetadataCommonKeyTitle] = it }
                }
                "artist" -> if (!metadataMap.containsKey(AVMetadataCommonKeyArtist)) {
                    value?.let { metadataMap[AVMetadataCommonKeyArtist] = it }
                }
                "album" -> if (!metadataMap.containsKey(AVMetadataCommonKeyAlbumName)) {
                    value?.let { metadataMap[AVMetadataCommonKeyAlbumName] = it }
                }
                "albumartist" -> if (!metadataMap.containsKey("albumArtist")) {
                    value?.let { metadataMap["albumArtist"] = it }
                }
                "date", "year" -> if (!metadataMap.containsKey(AVMetadataCommonKeyCreationDate)) {
                    value?.let { metadataMap[AVMetadataCommonKeyCreationDate] = it }
                }
                "tracknumber" -> if (!metadataMap.containsKey(AVMetadataID3MetadataKeyTrackNumber)) {
                    value?.let { metadataMap[AVMetadataID3MetadataKeyTrackNumber] = it }
                }
                "discnumber" -> if (!metadataMap.containsKey(AVMetadataIdentifieriTunesMetadataDiscNumber)) {
                    value?.let { metadataMap[AVMetadataIdentifieriTunesMetadataDiscNumber] = it }
                }
                "composer" -> if (!metadataMap.containsKey(AVMetadataID3MetadataKeyComposer)) {
                    value?.let { metadataMap[AVMetadataID3MetadataKeyComposer] = it }
                }
                "metadata_block_picture", "apic" -> {
                    // FLAC artwork
                    if (artworkBytes == null) {
                        val data = item.dataValue
                        if (data != null) {
                            artworkBytes = data.toByteArray()
                        }
                    }
                }
            }
        }

        println("MetadataParser, final metadata: $metadataMap")

        val durationMs: Long? = run {
            val seconds = CMTimeGetSeconds(asset.duration)
            if (seconds.isNaN() || seconds.isInfinite()) null else (seconds * 1000).toLong()
        }

        val fileName = url.lastPathComponent?.let { name ->
            val components = name.split(".")
            if (components.size > 1) {
                components.dropLast(1).joinToString(".")
            } else {
                name
            }
        }

        val title = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyTitle])
            ?: fileName
            ?: "Unknown title"

        return AudioMetadata(
            title = title,
            artist = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyArtist]),
            album = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyAlbumName]),
            albumArtist = commonMetadataParser.fixEncoding(metadataMap["albumArtist"]
                ?: metadataMap[AVMetadataCommonKeyArtist]),
            year = metadataMap[AVMetadataCommonKeyCreationDate],
            date = metadataMap[AVMetadataCommonKeyCreationDate],
            duration = durationMs,
            trackNumber = metadataMap[AVMetadataID3MetadataKeyTrackNumber],
            discNumber = metadataMap[AVMetadataIdentifieriTunesMetadataDiscNumber],
            composer = commonMetadataParser.fixEncoding(metadataMap[AVMetadataID3MetadataKeyComposer]),
            writer = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyAuthor]),
            bitrate = null,
            mimeType = null,
            artwork = artworkBytes
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getFullQualityArtwork(path: String): ByteArray? {
        val url = NSURL.fileURLWithPath(path)
        val asset = AVURLAsset.URLAssetWithURL(url, null)

        val commonMetadata = asset.commonMetadata
        for (item in commonMetadata) {
            if (item !is AVMetadataItem) continue
            val key = item.commonKey ?: continue

            if (key == AVMetadataCommonKeyArtwork) {
                val data = item.dataValue
                if (data != null) {
                    return data.toByteArray()
                }
            }
        }

        val formatMetadata = asset.metadata
        for (item in formatMetadata) {
            if (item !is AVMetadataItem) continue
            val key = item.key?.toString()?.lowercase() ?: continue

            if (key == "metadata_block_picture" || key == "apic") {
                val data = item.dataValue
                if (data != null) {
                    return data.toByteArray()
                }
            }
        }

        return null
    }
}