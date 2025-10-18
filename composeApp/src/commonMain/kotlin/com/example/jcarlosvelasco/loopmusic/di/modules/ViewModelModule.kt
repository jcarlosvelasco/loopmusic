package com.example.jcarlosvelasco.loopmusic.di.modules

import androidx.lifecycle.SavedStateHandle
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.PublishNowPlayingType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.SetPlayingType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.UpdateElapsedType
import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType
import com.example.jcarlosvelasco.loopmusic.presentation.album_detail.AlbumDetailScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.artist_detail.ArtistDetailScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.features.FeaturesViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.folder_songs.FolderSongsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.folders.FoldersScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.home.HomeScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.mediaFolders.MediaFoldersScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.navigator.NavigatorViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_detail.PlaylistDetailViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection.PlaylistSelectionViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlists.PlaylistsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.search.SearchScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class ViewModelModule {
    @KoinViewModel
    fun getHomeScreenViewModel(
    ): HomeScreenViewModel {
        return HomeScreenViewModel()
    }

    @KoinViewModel
    fun getMediaFoldersScreenViewModel(
        getSelectedMediaFolders: GetSelectedMediaFoldersType,
        getOpenFilePicker: OpenDirectoryPickerType,
        storeSelectedMediaFoldersType: StoreSelectedMediaFoldersType,
        getPermissionsType: GivePermissionsType,
        buildFolderTreeType: BuildFolderTreeType,
        savedStateHandle: SavedStateHandle
    ): MediaFoldersScreenViewModel {
        return MediaFoldersScreenViewModel(
            getSelectedMediaFolders,
            getOpenFilePicker,
            storeSelectedMediaFoldersType,
            getPermissionsType,
            buildFolderTreeType,
            savedStateHandle
        )
    }

    @KoinViewModel
    fun getNavigatorViewModel(
        getSelectedMediaFolders: GetSelectedMediaFoldersType,
    ): NavigatorViewModel {
        return NavigatorViewModel(getSelectedMediaFolders)
    }

    @Singleton
    @KoinViewModel
    fun getMainScreenViewModel(
        getSelectedMediaFolders: GetSelectedMediaFoldersType,
        cacheSongs: CacheSongsType,
        getCachedSongs: GetCachedSongsType,
        getFileList: GetFileListType,
        deleteSongsFromCache: DeleteSongsFromCacheType,
        readFileFromPath: ReadFileFromPathType,
        cleanUnusedArtwork: CleanUnusedArtworkType,
        cacheArtistArtwork: CacheArtistArtworkType,
        getArtistArtwork: GetArtistArtworkType,
        getCachedArtistArtworkType: GetCachedArtistArtworkType,
        getPlaylists: GetPlaylistsType,
        addSongsToPlaylistType: AddSongsToPlaylistType,
        deletePlaylist: DeletePlaylistType,
        renamePlaylistType: RenamePlaylistType,
        removeSongFromPlaylistType: RemoveSongFromPlaylistType
    ): MainScreenViewModel {
        return MainScreenViewModel(
            getSelectedMediaFolders = getSelectedMediaFolders,
            cacheSongs = cacheSongs,
            getCachedSongs = getCachedSongs,
            getFileList = getFileList,
            deleteSongsFromCache = deleteSongsFromCache,
            readFileFromPath = readFileFromPath,
            cleanUnusedArtwork = cleanUnusedArtwork,
            cacheArtistArtwork = cacheArtistArtwork,
            getArtistArtwork = getArtistArtwork,
            getCachedArtistArtwork = getCachedArtistArtworkType,
            getPlaylists = getPlaylists,
            addSongsToPlaylist = addSongsToPlaylistType,
            deletePlaylist = deletePlaylist,
            renamePlaylist = renamePlaylistType,
            removeSongFromPlaylist = removeSongFromPlaylistType
        )
    }

    @Singleton
    @KoinViewModel
    fun getFeaturesViewModel(
        getScreenFeatures: GetScreenFeaturesType,
        getSelectedScreenFeatures: GetSelectedScreenFeaturesType,
        setSelectedScreenFeatures: SetSelectedScreenFeaturesType
    ): FeaturesViewModel {
        return FeaturesViewModel(
            getScreenFeatures = getScreenFeatures,
            getSelectedScreenFeatures = getSelectedScreenFeatures,
            setSelectedScreenFeatures = setSelectedScreenFeatures
        )
    }

    @Singleton
    @KoinViewModel
    fun getAudioViewModel(
        playSongType: PlaySongType,
        pauseSongType: PauseSongType,
        resumeSong: ResumeSongType,
        seekTo: SeekToType,
        getListMode: GetListModeType,
        getPlayMode: GetPlayModeType,
        setListMode: SetListModeType,
        setPlayMode: SetPlayModeType,
        getCurrentMediaPlayerPosition: GetCurrentMediaPlayerPositionType,
        getCurrentPlayerState: GetCurrentPlayerStateType,
        publishNowPlaying: PublishNowPlayingType,
        updateElapsed: UpdateElapsedType,
        setPlaying: SetPlayingType,
        getPlaybackState: GetPlaybackStateType,
        savePlaybackState: SavePlaybackStateType,
        putSongInPlayerType: PutSongInPlayerType,
        getAlbumArtworkType: GetAlbumArtworkType,
        setOnSongEndCallbackType: SetOnSongEndCallbackType
    ): AudioViewModel {
        return AudioViewModel(
            playSong = playSongType,
            pauseSong = pauseSongType,
            resumeSong = resumeSong,
            seekTo = seekTo,
            getListMode = getListMode,
            getPlayMode = getPlayMode,
            setListMode = setListMode,
            setPlayMode = setPlayMode,
            getCurrentMediaPlayerPosition = getCurrentMediaPlayerPosition,
            getCurrentPlayerState = getCurrentPlayerState,
            publishNowPlaying = publishNowPlaying,
            updateElapsed = updateElapsed,
            setPlaying = setPlaying,
            getPlaybackState = getPlaybackState,
            savePlaybackState = savePlaybackState,
            putSongInPlayerType = putSongInPlayerType,
            getAlbumArtwork = getAlbumArtworkType,
            setOnSongEndCallback = setOnSongEndCallbackType
        )
    }

    @Singleton
    @KoinViewModel
    fun getPlayingScreenViewModel(
        getFullQualityArtworkType: GetFullQualityArtworkType
    ): PlayingScreenViewModel {
        return PlayingScreenViewModel(
            getFullQualityArtworkType
        )
    }

    @KoinViewModel
    fun getAlbumDetailViewModel(
        getFullQualityArtworkType: GetFullQualityArtworkType
    ): AlbumDetailScreenViewModel {
        return AlbumDetailScreenViewModel(
            getFullQualityArtworkType
        )
    }

    @Singleton
    @KoinViewModel
    fun getSettingsViewModel(
        getTheme: GetThemeType,
        setTheme: SetThemeType,
        statusBarManagerType: StatusBarManagerType
    ): ThemeViewModel {
        return ThemeViewModel(
            getTheme,
            setTheme,
            statusBarManagerType
        )
    }

    @Singleton
    @KoinViewModel
    fun getSearchViewModel(

    ): SearchScreenViewModel {
        return SearchScreenViewModel()
    }

    @KoinViewModel
    fun getArtistDetailViewModel(
    ): ArtistDetailScreenViewModel {
        return ArtistDetailScreenViewModel()
    }

    @KoinViewModel
    fun getFoldersScreenViewModel(
        getSelectedMediaFolders: GetSelectedMediaFoldersType
    ): FoldersScreenViewModel {
        return FoldersScreenViewModel(getSelectedMediaFolders)
    }

    @KoinViewModel
    fun getFolderSongsScreenViewModel(
        getFolderFromFolderPathType: GetFolderFromFolderPathType
    ): FolderSongsViewModel {
        return FolderSongsViewModel(
            getFolderFromFolderPathType
        )
    }

    @KoinViewModel
    fun getPlaylistsViewModel(
        storePlaylist: StorePlaylistType
    ): PlaylistsViewModel {
        return PlaylistsViewModel(
            storePlaylist = storePlaylist
        )
    }

    @KoinViewModel
    fun getPlaylistDetailScreenViewModel(
        getPlaylistFromPlaylistIdType: GetPlaylistFromPlaylistIdType
    ): PlaylistDetailViewModel {
        return PlaylistDetailViewModel(getPlaylistFromPlaylistIdType)
    }

    @Singleton
    @KoinViewModel
    fun getSongsViewModel(): SongsViewModel = SongsViewModel()

    @Singleton
    @KoinViewModel
    fun getPlaylistSelectionViewModel(): PlaylistSelectionViewModel = PlaylistSelectionViewModel()
}