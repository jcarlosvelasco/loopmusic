package com.example.jcarlosvelasco.loopmusic.infrastructure

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.core.graphics.scale
import androidx.core.net.toUri
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.domain.model.AudioMetadata
import com.example.jcarlosvelasco.loopmusic.utils.availableProcessors
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MetadataParser(
    private val context: Context,
    private val commonMetadataParser: CommonMetadataParser
): MetadataParserType {
    companion object {
        private const val MAX_ARTWORK_SIZE = 1024 * 1024
        private const val TARGET_IMAGE_SIZE = 512
        val parallelism = (availableProcessors() - 2).coerceAtLeast(2)
        private val parseSemaphore = Semaphore(parallelism)
    }

    override suspend fun parseMetadata(path: String): AudioMetadata {
        val retriever = MediaMetadataRetriever()

        try {
            val uri = path.toUri()

            return parseSemaphore.withPermit {
                withContext(Dispatchers.IO.limitedParallelism(2)) {
                    context.contentResolver.openFileDescriptor(uri, "r")?.use { fd ->
                        retriever.setDataSource(fd.fileDescriptor)
                    }
                }

                AudioMetadata(
                    title = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)),
                    artist = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)),
                    album = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)),
                    albumArtist = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)),
                    genre = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)),
                    year = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)),
                    date = commonMetadataParser.fixEncoding(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)),
                    duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull(),
                    trackNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER),
                    discNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER),
                    composer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER),
                    writer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER),
                    bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE),
                    mimeType = null,
                    artwork = retriever.embeddedPicture?.let { getOptimizedArtwork(it) }
                )
            }

        } catch (e: OutOfMemoryError) {
            log("MetadataParser","OutOfMemoryError parsing metadata for $path: ${e.message}")
            return AudioMetadata()
        } catch (e: Exception) {
            log("MetadataParser","Error parsing metadata for $path: ${e.message}")
            return AudioMetadata()
        } finally {
            try {
            } catch (e: Exception) {
                log("MetadataParser","Error releasing KTagLib: ${e.message}")
            }
        }
    }

    fun resizeArtwork(originalArtwork: ByteArray): ByteArray? {
        return try {
            val originalBitmap = BitmapFactory.decodeByteArray(originalArtwork, 0, originalArtwork.size)
                ?: return null

            val originalWidth = originalBitmap.width
            val originalHeight = originalBitmap.height
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

            val (newWidth, newHeight) = if (aspectRatio > 1) {
                TARGET_IMAGE_SIZE to (TARGET_IMAGE_SIZE / aspectRatio).toInt()
            } else {
                (TARGET_IMAGE_SIZE * aspectRatio).toInt() to TARGET_IMAGE_SIZE
            }

            val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val resizedArtwork = outputStream.toByteArray()

            originalBitmap.recycle()
            resizedBitmap.recycle()
            outputStream.close()

            resizedArtwork

        } catch (e: OutOfMemoryError) {
            log("MetadataParser", "OutOfMemoryError resizing artwork: ${e.message}")
            null
        } catch (e: Exception) {
            log("MetadataParser","Error resizing artwork: ${e.message}")
            null
        }
    }

    private fun getOptimizedArtwork(originalArtwork: ByteArray): ByteArray? {
        if (originalArtwork.size <= MAX_ARTWORK_SIZE) {
            return originalArtwork
        }
        return resizeArtwork(originalArtwork)
    }

    override suspend fun getFullQualityArtwork(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()

        try {
            val uri = path.toUri()

            context.contentResolver.openFileDescriptor(uri, "r")?.use { fd ->
                retriever.setDataSource(fd.fileDescriptor)
            }

            return retriever.embeddedPicture
        } catch (e: OutOfMemoryError) {
            log("MetadataParser","OutOfMemoryError parsing metadata for $path: ${e.message}")
            return null
        } catch (e: Exception) {
            log("MetadataParser","Error parsing metadata for $path: ${e.message}")
            return null
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                log("MetadataParser", "Error releasing MediaMetadataRetriever: ${e.message}")
            }
        }
    }
}