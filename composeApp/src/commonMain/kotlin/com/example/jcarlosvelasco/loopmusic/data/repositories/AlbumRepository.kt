package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetAlbumArtworkRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFullQualityArtworkRepoType

class AlbumRepository(
    private val files: FilesInfrType,
    private val metadataParser: MetadataParserType
):
    GetAlbumArtworkRepoType,
    GetFullQualityArtworkRepoType
{
    override suspend fun getAlbumArtwork(artworkHash: String): ByteArray? {
        return files.readCachedArtworkBytes(artworkHash, fromSongs = true)
    }

    override suspend fun getFullQualityArtwork(songPath: String): ByteArray? {
        return metadataParser.getFullQualityArtwork(songPath)
    }
}