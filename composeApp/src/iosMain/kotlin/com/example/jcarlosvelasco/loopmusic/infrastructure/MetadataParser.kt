package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.domain.model.AudioMetadata
import com.example.jcarlosvelasco.loopmusic.extensions.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSURL

class MetadataParser(
    private val commonMetadataParser: CommonMetadataParser
): MetadataParserType {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun parseMetadata(path: String): AudioMetadata {
        val url = NSURL.fileURLWithPath(path)
        val asset = AVURLAsset.URLAssetWithURL(url, null)

        val metadataMap = mutableMapOf<String, String>()

        val metadataList = asset.commonMetadata
        var artworkBytes: ByteArray? = null

        for (item in metadataList) {
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

        val durationMs: Long? = run {
            val seconds = CMTimeGetSeconds(asset.duration)
            if (seconds.isNaN() || seconds.isInfinite()) null else (seconds * 1000).toLong()
        }

        return AudioMetadata(
            title = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyTitle]),
            artist = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyArtist]),
            album = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyAlbumName]),
            albumArtist = commonMetadataParser.fixEncoding(metadataMap[AVMetadataCommonKeyArtist]),
            //genre = metadataMap[AVMetadata],
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

    override suspend fun getFullQualityArtwork(path: String): ByteArray? {
        val url = NSURL.fileURLWithPath(path)
        val asset = AVURLAsset.URLAssetWithURL(url, null)

        val metadataList = asset.commonMetadata
        var artworkBytes: ByteArray? = null

        for (item in metadataList) {
            if (item !is AVMetadataItem) continue

            val key = item.commonKey ?: continue

            if (key == AVMetadataCommonKeyArtwork) {
                val data = item.dataValue
                if (data != null) {
                    artworkBytes = data.toByteArray()
                }
            }
        }

        return artworkBytes
    }
}