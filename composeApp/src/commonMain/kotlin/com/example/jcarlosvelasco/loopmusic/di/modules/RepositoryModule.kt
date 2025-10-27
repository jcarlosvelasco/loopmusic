package com.example.jcarlosvelasco.loopmusic.di.modules

import com.example.jcarlosvelasco.loopmusic.data.interfaces.*
import com.example.jcarlosvelasco.loopmusic.data.mapper.*
import com.example.jcarlosvelasco.loopmusic.data.remote.api.SpotifyApiService
import com.example.jcarlosvelasco.loopmusic.data.repositories.*
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.*
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.MediaFoldersDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.PlaylistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.SongDao
import com.example.jcarlosvelasco.loopmusic.secrets.SPOTIFY_CLIENT_ID
import com.example.jcarlosvelasco.loopmusic.secrets.SPOTIFY_CLIENT_SECRET
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class RepositoryModule {

    @Single
    fun getPlaybackRepository(
        sharedPreferences: SharedPreferencesInfrType
    ): PlaybackRepository {
        return PlaybackRepository(sharedPreferences)
    }

    @Single
    fun getFeatureRepository(
        sharedPreferences: SharedPreferencesInfrType
    ): FeatureRepository {
        return FeatureRepository(sharedPreferences)
    }

    @Single
    fun getFolderRepository(
        mediaFoldersDao: MediaFoldersDao,
        folderEntityMapper: FolderEntityMapper,
    ): FolderRepository {
        return FolderRepository(
            folderEntityMapper = folderEntityMapper,
            mediaFoldersDao = mediaFoldersDao
        )
    }

    @Single
    fun getArtistRepository(
        apiService: SpotifyApiService,
        artistInfoResponseMapper: ArtistResponseMapper,
        files: FilesInfrType
    ): ArtistRepository {
        return ArtistRepository(
            apiService = apiService,
            artistResponseMapper = artistInfoResponseMapper,
            files = files
        )
    }

    @Single
    fun getPlaylistRepository(
        playlistDao: PlaylistDao,
        playlistWithSongsMapper: PlaylistWithSongsMapper,
        playlistMapper: PlaylistMapper,
    ): PlaylistRepository {
        return PlaylistRepository(
            playlistDao = playlistDao,
            playlistWithSongsMapper = playlistWithSongsMapper,
            playlistMapper = playlistMapper,
        )
    }

    @Single
    fun getFilesRepository(
        filePicker: FilesPickerInfrType,
        entityMapper: FolderEntityMapper,
        folderMapper: FolderMapper,
        mediaFoldersDao: MediaFoldersDao,
        metadataParser: MetadataParserType,
        audioMetadataMapper: AudioMetadataMapper,
        files: FilesInfrType
    ): FilesRepository {
        return FilesRepository(
            filePicker,
            mediaFoldersDao,
            entityMapper,
            folderMapper,
            metadataParser = metadataParser,
            audioMetadataMapper = audioMetadataMapper,
            files = files
        )
    }

    @Single
    fun getThemeRepository(
        sharedPreferences: SharedPreferencesInfrType
    ): ThemeRepository {
        return ThemeRepository(sharedPreferences)
    }

    @Single
    fun getAlbumRepository(
        files: FilesInfrType,
        metadataParser: MetadataParserType
    ): AlbumRepository {
        return AlbumRepository(files, metadataParser)
    }

    @Single
    fun getSongRepository(
        songDao: SongDao,
        songMapper: SongMapper,
        artistMapper: ArtistMapper,
        songWithArtistMapper: SongWithArtistMapper,
        albumMapper: AlbumMapper,
        files: FilesInfrType,
        mediaPlayer: MediaPlayerInfrType,
        sharedPreferences: SharedPreferencesInfrType,
        playbackService: PlaybackServiceType?
    ): SongRepository {
        return SongRepository(
            songDao = songDao,
            songMapper = songMapper,
            artistMapper = artistMapper,
            songWithArtistMapper = songWithArtistMapper,
            albumMapper = albumMapper,
            files = files,
            mediaPlayer = mediaPlayer,
            sharedPreferences = sharedPreferences,
            playbackService = playbackService
        )
    }

    @Single
    fun getGetSelectedMediaFoldersRepo(filesRepo: FilesRepository): GetSelectedMediaFoldersRepositoryType {
        return filesRepo
    }

    @Single
    fun getOpenFilePickerRepo(filesRepo: FilesRepository): OpenDirectoryPickerRepositoryType {
        return filesRepo
    }

    @Single
    fun getStoreSelectedMediaFoldersRepo(filesRepo: FilesRepository): StoreSelectedMediaFoldersRepositoryType {
        return filesRepo
    }

    @Single
    fun getReadFileFromPathRepo(filesRepo: FilesRepository): ReadFileFromPathRepositoryType {
        return filesRepo
    }

    @Single
    fun getFilesFromFolderRepositoryType(filesRepo: FilesRepository): GetFilesFromFolderRepositoryType {
        return filesRepo
    }

    @Single
    fun getGivePermissionsRepositoryType(filesRepo: FilesRepository): GivePermissionsRepositoryType {
        return filesRepo
    }

    @Single
    fun getBuildFolderTreeRepositoryType(filesRepo: FilesRepository): BuildFolderTreeRepositoryType {
        return filesRepo
    }

    @Single
    fun getCacheSongsRepositoryType(songRepo: SongRepository): CacheSongsRepositoryType {
        return songRepo
    }

    @Single
    fun getDeleteSongsFromCacheRepositoryType(songRepo: SongRepository): DeleteSongsFromCacheRepoType {
        return songRepo
    }

    @Single
    fun getGetCachedSongsRepositoryType(songRepo: SongRepository): GetCachedSongsRepositoryType {
        return songRepo
    }

    @Single
    fun getCleanUnusedArtworkRepositoryType(songRepo: SongRepository): CleanUnusedArtworkRepoType {
        return songRepo
    }

    @Single
    fun getPlaySongRepositoryType(songRepo: SongRepository): PlaySongRepoType {
        return songRepo
    }

    @Single
    fun getPauseSongRepositoryType(songRepo: SongRepository): PauseSongRepoType {
        return songRepo
    }

    @Single
    fun getResumeSongRepositoryType(songRepo: SongRepository): ResumeSongRepoType {
        return songRepo
    }

    @Single
    fun getSeekToRepositoryType(songRepo: SongRepository): SeekToRepoType {
        return songRepo
    }

    @Single
    fun getGetPlayModeRepositoryType(songRepo: SongRepository): GetPlayModeRepoType {
        return songRepo
    }

    @Single
    fun getSetPlayModeRepositoryType(songRepo: SongRepository): SetPlayModeRepoType {
        return songRepo
    }

    @Single
    fun getGetListModeRepositoryType(songRepo: SongRepository): GetListModeRepoType {
        return songRepo
    }

    @Single
    fun getSetListModeRepositoryType(songRepo: SongRepository): SetListModeRepoType {
        return songRepo
    }

    @Single
    fun getGetCurrentPlayerPositionRepositoryType(songRepo: SongRepository): GetCurrentPlayerPositionRepoType {
        return songRepo
    }

    @Single
    fun getGetCurrentPlayerStateRepositoryType(songRepo: SongRepository): GetCurrentPlayerStateRepoType {
        return songRepo
    }

    @Single
    fun getPublishNowPlayingRepositoryType(songRepo: SongRepository): PublishNowPlayingRepoType {
        return songRepo
    }

    @Single
    fun getUpdateElapsedRepositoryType(songRepo: SongRepository): UpdateElapsedRepoType {
        return songRepo
    }

    @Single
    fun getSetPlayingRepositoryType(songRepo: SongRepository): SetPlayingRepoType {
        return songRepo
    }

    @Single
    fun getGetPlaybackStateRepoType(repo: PlaybackRepository): GetPlaybackStateRepoType = repo

    @Single
    fun getSavePlaybackStateRepoType(repo: PlaybackRepository): SavePlaybackStateRepoType = repo

    @Single
    fun getPutSongInPlayerRepoType(repo: SongRepository): PutSongInPlayerRepoType = repo

    @Single
    fun getGetAlbumArtworkRepoType(repo: AlbumRepository): GetAlbumArtworkRepoType = repo

    @Single
    fun getSetOnSongEndCallbackRepoType(repo: SongRepository): SetOnSongEndCallbackRepoType = repo

    @Single
    fun getGetSelectedScreenFeaturesRepoType(repo: FeatureRepository): GetSelectedScreenFeaturesRepoType = repo

    @Single
    fun getSetSelectedScreenFeaturesRepoType(repo: FeatureRepository): SetSelectedScreenFeaturesRepoType = repo

    @Single
    fun getGetThemeRepoType(repo: ThemeRepository): GetThemeRepositoryType = repo

    @Single
    fun getSetThemeRepoType(repo: ThemeRepository): SetThemeRepositoryType = repo

    @Single
    fun getGetFullQualityArtworkRepoType(repo: AlbumRepository): GetFullQualityArtworkRepoType = repo

    @Single
    fun getGetArtistArtworkRepoType(repo: ArtistRepository): GetArtistArtworkRepoType = repo

    @Single
    fun getApiService(
        client: HttpClientType
    ): SpotifyApiService {
        return SpotifyApiService(
            httpClient = client,
            clientId = SPOTIFY_CLIENT_ID,
            clientSecret = SPOTIFY_CLIENT_SECRET
        )
    }

    @Single
    fun getCacheArtistArtworkRepoType(repo: ArtistRepository): CacheArtistArtworkRepoType = repo

    @Single
    fun getGetCachedArtistArtworkRepoType(repo: ArtistRepository): GetCachedArtistArtworkRepoType = repo

    @Single
    fun getGetFolderFromFolderPathRepoType(repo: FolderRepository): GetFolderFromFolderPathRepoType = repo

    @Single
    fun getGetPlaylistsRepoType(repo: PlaylistRepository): GetPlaylistsRepoType = repo

    @Single
    fun getStorePlaylistRepoType(repo: PlaylistRepository): StorePlaylistRepoType = repo

    @Single
    fun getPlaylistFromPlaylistIdRepoType(repo: PlaylistRepository): GetPlaylistFromPlaylistIdRepoType = repo

    @Single
    fun getAddSongsToPlaylistRepoType(repo: PlaylistRepository): AddSongsToPlaylistRepoType = repo

    @Single
    fun getDeletePlaylistRepoType(repo: PlaylistRepository): DeletePlaylistRepoType = repo

    @Single
    fun getRenamePlaylistRepoType(repo: PlaylistRepository): RenamePlaylistRepoType = repo

    @Single
    fun getRemoveSongFromPlaylistRepoType(repo: PlaylistRepository): RemoveSongFromPlaylistRepoType = repo

    @Single
    fun getCacheAlbumArtworkRepoType(repo: AlbumRepository): CacheAlbumArtworkRepoType = repo
}