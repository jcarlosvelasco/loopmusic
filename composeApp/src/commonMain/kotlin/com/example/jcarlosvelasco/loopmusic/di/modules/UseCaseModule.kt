package com.example.jcarlosvelasco.loopmusic.di.modules

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class UseCaseModule {
    @Single
    fun getGetSelectedMediaFolders(repo: GetSelectedMediaFoldersRepositoryType): GetSelectedMediaFoldersType {
        return GetSelectedMediaFolders(repo)
    }

    @Single
    fun getOpenFilePicker(repo: OpenDirectoryPickerRepositoryType): OpenDirectoryPickerType {
        return OpenDirectoryPicker(repo)
    }

    @Single
    fun getUpdateFolderSelection(): UpdateFolderSelectionType {
        return UpdateFolderSelection()
    }

    @Single
    fun getStoreSelectedMediaFolders(repo: StoreSelectedMediaFoldersRepositoryType): StoreSelectedMediaFoldersType {
        return StoreSelectedMediaFolders(repo)
    }

    @Single
    fun getReadFilesFromFolders(
        getFilesFromFolderRepo: GetFilesFromFolderRepositoryType,
        readFileFromPathRepo: ReadFileFromPathRepositoryType
    ): ReadFilesFromFoldersType {
        return ReadFilesFromFolders(
            getFilesFromFolderRepo = getFilesFromFolderRepo,
            readFileFromPath = readFileFromPathRepo
        )
    }

    @Single
    fun getGivePermissions(
        getPermissionsRepositoryType: GivePermissionsRepositoryType
    ): GivePermissionsType {
        return GivePermissions(getPermissionsRepositoryType)
    }

    @Single
    fun getBuildFolderTree(
        repo: BuildFolderTreeRepositoryType
    ): BuildFolderTreeType {
        return BuildFolderTree(repo)
    }

    @Single
    fun getCacheSongs(
        repo: CacheSongsRepositoryType
    ): CacheSongsType {
        return CacheSongs(repo)
    }

    @Single
    fun getGetCachedSongs(
        repo: GetCachedSongsRepositoryType
    ): GetCachedSongsType {
        return GetCachedSongs(repo)
    }

    @Single
    fun getGetFileList(
        repo: GetFilesFromFolderRepositoryType
    ): GetFileListType {
        return GetFileList(repo)
    }

    @Single
    fun getDeleteSongsFromCache(
        repo: DeleteSongsFromCacheRepoType
    ): DeleteSongsFromCacheType {
        return DeleteSongsFromCache(repo)
    }

    @Single
    fun getReadFileFromPath(
        repo: ReadFileFromPathRepositoryType
    ): ReadFileFromPathType {
        return ReadFileFromPath(repo)
    }

    @Single
    fun getCleanUnusedArtwork(
        repo: CleanUnusedArtworkRepoType
    ): CleanUnusedArtworkType {
        return CleanUnusedArtwork(repo)
    }

    @Single
    fun getPlaySong(
        repo: PlaySongRepoType
    ): PlaySongType {
        return PlaySong(repo)
    }

    @Single
    fun getPauseSong(
        repo: PauseSongRepoType
    ): PauseSongType {
        return PauseSong(repo)
    }

    @Single
    fun getResumeSong(
        repo: ResumeSongRepoType
    ): ResumeSongType {
        return ResumeSong(repo)
    }

    @Single
    fun getSeekTo(
        repo: SeekToRepoType
    ): SeekToType {
        return SeekTo(repo)
    }

    @Single
    fun getGetPlayMode(
        repo: GetPlayModeRepoType
    ): GetPlayModeType {
        return GetPlayMode(repo)
    }

    @Single
    fun getSetPlayMode(
        repo: SetPlayModeRepoType
    ): SetPlayModeType {
        return SetPlayMode(repo)
    }

    @Single
    fun getGetListMode(
        repo: GetListModeRepoType
    ): GetListModeType {
        return GetListMode(repo)
    }

    @Single
    fun getSetListMode(
        repo: SetListModeRepoType
    ): SetListModeType {
        return SetListMode(repo)
    }

    @Single
    fun getCurrentPlayerPosition(
        repo: GetCurrentPlayerPositionRepoType
    ): GetCurrentMediaPlayerPositionType {
        return GetCurrentMediaPlayerPosition(repo)
    }

    @Single
    fun getCurrentPlayerState(
        repo: GetCurrentPlayerStateRepoType
    ): GetCurrentPlayerStateType {
        return GetCurrentPlayerState(repo)
    }

    @Single
    fun getPublishNowPlaying(
        repo: PublishNowPlayingRepoType
    ): PublishNowPlayingType {
        return PublishNowPlaying(repo)
    }

    @Single
    fun getUpdateElapsed(
        repo: UpdateElapsedRepoType
    ): UpdateElapsedType {
        return UpdateElapsed(repo)
    }

    @Single
    fun getSetPlaying(
        repo: SetPlayingRepoType
    ): SetPlayingType {
        return SetPlaying(repo)
    }

    @Single
    fun getGetPlaybackState(
        repo: GetPlaybackStateRepoType
    ): GetPlaybackStateType {
        return GetPlaybackState(repo)
    }

    @Single
    fun getSavePlaybackState(
        repo: SavePlaybackStateRepoType
    ): SavePlaybackStateType {
        return SavePlaybackState(repo)
    }

    @Single
    fun getPutSongInPlayer(
        repo: PutSongInPlayerRepoType
    ): PutSongInPlayerType {
        return PutSongInPlayer(repo)
    }

    @Single
    fun getGetAlbumArtwork(
        repo: GetAlbumArtworkRepoType
    ): GetAlbumArtworkType {
        return GetAlbumArtwork(repo)
    }

    @Single
    fun getSetOnSongEndCallback(
        repo: SetOnSongEndCallbackRepoType
    ): SetOnSongEndCallbackType {
        return SetOnSongEndCallback(repo)
    }

    @Single
    fun getGetScreenFeatures(

    ): GetScreenFeaturesType {
        return GetScreenFeatures()
    }

    @Single
    fun getGetSelectedScreenFeatures(
        repo: GetSelectedScreenFeaturesRepoType
    ): GetSelectedScreenFeaturesType {
        return GetSelectedScreenFeatures(repo)
    }

    @Single
    fun getSetSelectedScreenFeatures(
        repo: SetSelectedScreenFeaturesRepoType
    ): SetSelectedScreenFeaturesType {
        return SetSelectedScreenFeatures(repo)
    }

    @Single
    fun getGetTheme(
        repo: GetThemeRepositoryType
    ): GetThemeType {
        return GetTheme(repo)
    }

    @Single
    fun getSetTheme(
        repo: SetThemeRepositoryType
    ): SetThemeType {
        return SetTheme(repo)
    }

    @Single
    fun getGetFullQualityArtwork(
        repo: GetFullQualityArtworkRepoType
    ): GetFullQualityArtworkType {
        return GetFullQualityArtwork(repo)
    }

    @Single
    fun getArtistArtwork(
        repo: GetArtistArtworkRepoType
    ): GetArtistArtworkType {
        return GetArtistArtwork(repo)
    }

    @Single
    fun getCacheArtistArtwork(
        repo: CacheArtistArtworkRepoType
    ): CacheArtistArtworkType {
        return CacheArtistArtwork(repo)
    }

    @Single
    fun getGetCachedArtistArtwork(
        repo: GetCachedArtistArtworkRepoType
    ): GetCachedArtistArtworkType {
        return GetCachedArtistArtwork(repo)
    }

    @Single
    fun getGetFolderFromFolderPath(
        repo: GetFolderFromFolderPathRepoType
    ): GetFolderFromFolderPathType {
        return GetFolderFromFolderPath(repo)
    }

    @Single
    fun getGetPlaylists(
        repo: GetPlaylistsRepoType
    ): GetPlaylistsType {
        return GetPlaylists(repo)
    }

    @Single
    fun getStorePlaylist(repo: StorePlaylistRepoType): StorePlaylistType = StorePlaylist(repo)

    @Single
    fun getGetPlaylistFromPlaylistId(repo: GetPlaylistFromPlaylistIdRepoType): GetPlaylistFromPlaylistIdType = GetPlaylistFromPlaylistId(repo)

    @Single
    fun getAddSongsToPlaylist(repo: AddSongsToPlaylistRepoType): AddSongsToPlaylistType = AddSongsToPlaylist(repo)

    @Single
    fun getDeletePlaylist(repo: DeletePlaylistRepoType): DeletePlaylistType = DeletePlaylist(repo)

    @Single
    fun getRenamePlaylist(repo: RenamePlaylistRepoType): RenamePlaylistType = RenamePlaylist(repo)

    @Single
    fun getRemoveSongFromPlaylist(repo: RemoveSongFromPlaylistRepoType): RemoveSongFromPlaylistType = RemoveSongFromPlaylist(repo)

    @Single
    fun getCacheAlbumArtwork(repo: CacheAlbumArtworkRepoType): CacheAlbumArtworkType = CacheAlbumArtwork(repo)
}