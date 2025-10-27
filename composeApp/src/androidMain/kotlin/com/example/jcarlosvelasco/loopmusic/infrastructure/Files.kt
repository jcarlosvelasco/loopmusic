package com.example.jcarlosvelasco.loopmusic.infrastructure

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.domain.config.allowedExtensions
import com.example.jcarlosvelasco.loopmusic.domain.config.notAllowedFileStartingNames
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class Files(
    private val context: Context
): FilesInfrType {
    private val externalCacheFolderName = "artwork_cache_external"
    private val cacheFolderName = "artwork_cache"
    private val artistCacheFolderName = "artist_cache"

    override fun listSubdirectories(path: String): List<String> {
        val uri = path.toUri()
        val documentDir = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()

        return documentDir.listFiles()
            .filter { it.isDirectory && !it.name.orEmpty().startsWith(".") }
            .map { it.uri.toString() }
    }

    override fun listFiles(path: String): List<File> {
        val uri = path.toUri()
        val documentDir = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()

        return documentDir.listFiles()
            .filter { it.isFile && allowedExtensions.any { ext -> it.name.orEmpty().endsWith(".$ext", ignoreCase = true) } }
            .filter { file ->
                notAllowedFileStartingNames.none { prefix ->
                    file.name.orEmpty().startsWith(prefix, ignoreCase = true)
                }
            }
            .map {
                File(
                    path = it.uri.toString(),
                    modificationDate = it.lastModified()
                )
            }
    }

    override fun takePersistablePermissionIfNeeded(uriString: String) {
        val uri = uriString.toUri()
        try {
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: SecurityException) {
            log("Files", "Error taking persistable permission: ${e.message}")
        }
    }

    override suspend fun storeImageInFolder(artwork: ByteArray, songIdentifier: String, isExternal: Boolean) {
        try {
            val folderName = if (isExternal) externalCacheFolderName else cacheFolderName
            val baseDirectory = java.io.File(context.getExternalFilesDir(null), folderName)

            if (!baseDirectory.exists()) {
                val created = baseDirectory.mkdirs()
                if (created) {
                    println("Directorio creado: ${baseDirectory.absolutePath}")
                } else {
                    println("No se pudo crear el directorio: ${baseDirectory.absolutePath}")
                    return
                }
            }

            val file = java.io.File(baseDirectory, "${songIdentifier}.jpg")

            if (file.exists()) {
                log("Files","El archivo ya existe")
                return
            }

            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(artwork)
                    outputStream.flush()
                }
            }

            println("Archivo guardado exitosamente en: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al guardar el archivo: ${e.message}")
        }
    }

    override suspend fun storeArtistArtwork(artwork: ByteArray, artistIdentifier: String) {
        try {
            val baseDirectory = java.io.File(context.getExternalFilesDir(null), artistCacheFolderName)

            if (!baseDirectory.exists()) {
                val created = baseDirectory.mkdirs()
                if (created) {
                    println("Directorio creado: ${baseDirectory.absolutePath}")
                } else {
                    println("No se pudo crear el directorio: ${baseDirectory.absolutePath}")
                    return
                }
            }

            val file = java.io.File(baseDirectory, "${artistIdentifier}.jpg")

            if (file.exists()) {
                log("Files","El archivo ya existe")
                return
            }

            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(artwork)
                    outputStream.flush()
                }
            }

            println("Archivo guardado exitosamente en: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al guardar el archivo: ${e.message}")
        }
    }

    override suspend fun readCachedArtworkBytes(identifier: String, fromSongs: Boolean, isExternal: Boolean): ByteArray? {
        val folder = if (fromSongs && isExternal) externalCacheFolderName else if (fromSongs) cacheFolderName else artistCacheFolderName
        val baseDirectory = java.io.File(context.getExternalFilesDir(null), folder)
        val file = java.io.File(baseDirectory, "${identifier}.jpg")
        return if (file.exists()) withContext(Dispatchers.IO) { file.readBytes() } else null
    }

    override suspend fun removeImageFromCache(songIdentifier: String) {
        withContext(Dispatchers.IO) {
            try {
                val baseDirectory = java.io.File(context.getExternalFilesDir(null), cacheFolderName)
                val file = java.io.File(baseDirectory, "${songIdentifier}.jpg")

                if (file.exists()) {
                    val deleted = file.delete()
                    if (deleted) {
                        log("Files", "Archivo eliminado: ${file.absolutePath}")
                    } else {
                        log("Files", "No se pudo eliminar el archivo: ${file.absolutePath}")
                    }
                } else {
                    log("Files",  "El archivo no existe: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                log("Files","Error al eliminar archivo de caché: ${e.message}")
            }
        }
    }

    override fun listCachedArtworkIdentifiers(): List<String> {
        val baseDirectory = java.io.File(context.getExternalFilesDir(null), cacheFolderName)
        if (!baseDirectory.exists() || !baseDirectory.isDirectory) return emptyList()

        val allowedExts = setOf(".jpg")

        return baseDirectory.listFiles()
            ?.asSequence()
            ?.filter { it.isFile && it.name != null }
            ?.mapNotNull { file ->
                val name = file.name
                if (name.startsWith(".")) return@mapNotNull null
                val ext = allowedExts.firstOrNull { name.endsWith(it, ignoreCase = true) } ?: return@mapNotNull null
                name.removeSuffix(ext)
            }
            ?.toList()
            ?: emptyList()
    }
}