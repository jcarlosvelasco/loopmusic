package com.example.jcarlosvelasco.loopmusic.data.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.File

interface FilesInfrType {
    fun listSubdirectories(path: String): List<String>
    fun listFiles(path: String): List<File>
    fun takePersistablePermissionIfNeeded(uriString: String)
    suspend fun storeImageInFolder(artwork: ByteArray, songIdentifier: String, isExternal: Boolean)
    suspend fun readCachedArtworkBytes(identifier: String, fromSongs: Boolean, isExternal: Boolean): ByteArray?

    suspend fun removeImageFromCache(songIdentifier: String)
    fun listCachedArtworkIdentifiers(): List<String>
    suspend fun storeArtistArtwork(artwork: ByteArray, artistIdentifier: String)
}
