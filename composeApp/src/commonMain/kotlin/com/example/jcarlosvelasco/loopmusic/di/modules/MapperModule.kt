package com.example.jcarlosvelasco.loopmusic.di.modules

import com.example.jcarlosvelasco.loopmusic.data.interfaces.HttpClientType
import com.example.jcarlosvelasco.loopmusic.data.mapper.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class MapperModule {
    @Single
    fun getAudioMetadataMapper(): AudioMetadataMapper {
        return AudioMetadataMapper()
    }

    @Single
    fun getSongMapper(): SongMapper {
        return SongMapper()
    }

    @Single
    fun getArtistMapper(): ArtistMapper {
        return ArtistMapper()
    }

    @Single
    fun getArtistEntityMapper(): ArtistEntityMapper {
        return ArtistEntityMapper()
    }

    @Single
    fun getSongWithArtistMapper(
        artistMapper: ArtistEntityMapper,
        albumMapper: AlbumEntityMapper
    ): SongWithArtistMapper {
        return SongWithArtistMapper(artistMapper = artistMapper, albumMapper = albumMapper)
    }

    @Single
    fun getAlbumEntityMapper(
        artistMapper: ArtistEntityMapper
    ): AlbumEntityMapper {
        return AlbumEntityMapper(artistMapper)
    }

    @Single
    fun getAlbumMapper(): AlbumMapper {
        return AlbumMapper()
    }

    @Single
    fun getSongEntityMapper(
        albumEntityMapper: AlbumEntityMapper,
        artistEntityMapper: ArtistEntityMapper
    ): SongEntityMapper {
        return SongEntityMapper(
            albumEntityMapper = albumEntityMapper,
            artistEntityMapper = artistEntityMapper
        )
    }

    @Single
    fun getArtistResponseMapper(
        httpClient: HttpClientType
    ): ArtistResponseMapper {
        return ArtistResponseMapper(httpClient)
    }

    @Single
    fun getEntityMapper(): FolderEntityMapper {
        return FolderEntityMapper()
    }

    @Single
    fun getFolderMapper(): FolderMapper {
        return FolderMapper()
    }

    @Single
    fun getPlaylistWithSongsMapper(): PlaylistWithSongsMapper {
        return PlaylistWithSongsMapper()
    }

    @Single
    fun getPlaylistMapper(): PlaylistMapper {
        return PlaylistMapper()
    }
}