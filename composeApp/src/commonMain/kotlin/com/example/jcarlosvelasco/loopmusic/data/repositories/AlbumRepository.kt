package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.CacheAlbumArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetAlbumArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFullQualityArtworkRepoType

class AlbumRepository(
    private val files: FilesInfrType,
    private val metadataParser: MetadataParserType
):
    GetAlbumArtworkRepoType,
    GetFullQualityArtworkRepoType,
    CacheAlbumArtworkRepoType
{
    override suspend fun getAlbumArtwork(artworkHash: String, isExternal: Boolean): ByteArray? {
        return files.readCachedArtworkBytes(artworkHash, fromSongs = true, isExternal = isExternal)
    }

    override suspend fun getFullQualityArtwork(songPath: String): ByteArray? {
        return metadataParser.getFullQualityArtwork(songPath)
    }

    override suspend fun cacheAlbumArtwork(identifier: String, image: ByteArray) {
        files.storeImageInFolder(image, identifier, isExternal = true)
    }
}