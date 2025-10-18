package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.domain.config.allowedExtensions
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.extensions.toByteArray
import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.*

class Files: FilesInfrType {
    private val cacheFolderName = "artwork_cache"
    private val artistCacheFolderName = "artist_cache"


    @OptIn(ExperimentalForeignApi::class)
    override fun listSubdirectories(path: String): List<String> {
        val fileManager = NSFileManager.defaultManager
        val contents = fileManager.contentsOfDirectoryAtPath(path, error = null) ?: return emptyList()

        return contents.mapNotNull { name ->
            val fullPath = "$path/$name"
            memScoped {
                val isDir = alloc<BooleanVar>()
                val exists = fileManager.fileExistsAtPath(fullPath, isDirectory = isDir.ptr)
                if (exists && isDir.value) fullPath else null
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun listFiles(path: String): List<File> {
        val fileManager = NSFileManager.defaultManager
        val url = NSURL.fileURLWithPath(path)

        val contents = fileManager.contentsOfDirectoryAtURL(
            url,
            includingPropertiesForKeys = listOf(NSURLContentModificationDateKey),
            options = NSDirectoryEnumerationSkipsHiddenFiles,
            error = null
        ) ?: return emptyList()

        return contents.filterIsInstance<NSURL>().filter { fileUrl ->
            val pathExtension = fileUrl.pathExtension ?: ""
            allowedExtensions.any { it.equals(pathExtension, ignoreCase = true) }
        }.map { fileUrl ->
            val modificationDate = fileUrl
                .resourceValuesForKeys(listOf(NSURLContentModificationDateKey), null)
                ?.get(NSURLContentModificationDateKey) as? NSDate

            val modificationTimeMillis = modificationDate
                ?.timeIntervalSince1970
                ?.times(1000)
                ?.toLong()
                ?: 0L

            File(
                path = fileUrl.path!!,
                modificationDate = modificationTimeMillis
            )
        }
    }

    override fun takePersistablePermissionIfNeeded(uriString: String) {

    }

    @OptIn(ExperimentalForeignApi::class)
    private fun getBaseDirectory(fromSongs: Boolean): NSURL {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        )

        val basePath: String = (paths.firstOrNull() as? String) ?: NSTemporaryDirectory()
        val folderPath = if (fromSongs) "$basePath/$cacheFolderName" else "$basePath/$artistCacheFolderName"

        val fileManager = NSFileManager.defaultManager
        if (!fileManager.fileExistsAtPath(folderPath)) {
            fileManager.createDirectoryAtPath(folderPath, withIntermediateDirectories = true, attributes = null, error = null)
        }

        return NSURL.fileURLWithPath(folderPath)
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun storeImageInFolder(artwork: ByteArray, songIdentifier: String) {
        val fileURL = getBaseDirectory(true).URLByAppendingPathComponent("$songIdentifier.jpg")
        val nsData = artwork.usePinned { pinned ->
            NSData.dataWithBytes(bytes = pinned.addressOf(0), length = artwork.size.toULong())
        }

        fileURL?.let {
            withContext(Dispatchers.IO) {
                nsData.writeToURL(it, atomically = true)
            }
        }
    }

    override suspend fun readCachedArtworkBytes(identifier: String, fromSongs: Boolean): ByteArray? {
        val fileURL = getBaseDirectory(fromSongs).URLByAppendingPathComponent("$identifier.jpg")
        fileURL?.let {
            val data = NSData.dataWithContentsOfURL(fileURL) ?: return null
            return data.toByteArray()
        }
        return null
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun storeArtistArtwork(artwork: ByteArray, artistIdentifier: String) {
        val fileURL = getBaseDirectory(false).URLByAppendingPathComponent("$artistIdentifier.jpg")
        val nsData = artwork.usePinned { pinned ->
            NSData.dataWithBytes(bytes = pinned.addressOf(0), length = artwork.size.toULong())
        }

        fileURL?.let {
            withContext(Dispatchers.IO) {
                nsData.writeToURL(it, atomically = true)
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun removeImageFromCache(songIdentifier: String) {
        withContext(Dispatchers.IO) {
            try {
                val fileURL = getBaseDirectory(true).URLByAppendingPathComponent("$songIdentifier.jpg")
                val fileManager = NSFileManager.defaultManager

                if (fileURL != null && fileManager.fileExistsAtPath(fileURL.path!!)) {
                    val success = memScoped {
                        val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                        fileManager.removeItemAtURL(fileURL, error = errorPtr.ptr)
                        errorPtr.value == null
                    }

                    if (success) {
                        println("Archivo eliminado: ${fileURL.path}")
                    } else {
                        println("No se pudo eliminar el archivo: ${fileURL.path}")
                    }
                } else {
                    println("El archivo no existe: ${fileURL?.path}")
                }
            } catch (e: Exception) {
                println("Error eliminando archivo de caché: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun listCachedArtworkIdentifiers(): List<String> {
        val dirUrl = getBaseDirectory(true)
        val fileManager = NSFileManager.defaultManager

        val contents = fileManager.contentsOfDirectoryAtURL(
            dirUrl,
            includingPropertiesForKeys = emptyList<Any>(),
            options = NSDirectoryEnumerationSkipsHiddenFiles,
            error = null
        ) ?: return emptyList()

        val allowedExts = setOf("jpg")

        return contents
            .filterIsInstance<NSURL>()
            .mapNotNull { url ->
                val name = url.lastPathComponent ?: return@mapNotNull null
                if (name.startsWith(".")) return@mapNotNull null
                val ext = url.pathExtension ?: ""
                if (!allowedExts.any { it.equals(ext, ignoreCase = true) }) return@mapNotNull null
                // Remove the filename extension to obtain the identifier (hash)
                name.removeSuffix(".${ext}")
            }
    }
}