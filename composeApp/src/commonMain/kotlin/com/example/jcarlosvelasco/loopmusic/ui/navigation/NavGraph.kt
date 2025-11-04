package com.example.jcarlosvelasco.loopmusic.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.jcarlosvelasco.loopmusic.presentation.artist_detail.ArtistDetailScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.features.FeaturesViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.navigator.NavigatorViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection.PlaylistSelectionViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlists.PlaylistsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.album_detail.AlbumDetailScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.albums.AlbumsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.artist_detail.ArtistDetailScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.artists.ArtistsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.folder_songs.FoldersSongsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.folders.FoldersScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.home.HomeScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.loading.LoadingScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.main.MainScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders.MediaFoldersScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.PlayingScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.playlist_detail.PlaylistDetailScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.playlists.PlaylistsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.search.SearchScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.settings.SettingsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.settings_tabs.SettingsTabsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongsScreen
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CreateNavGraph(
    navController: NavHostController,
    viewModel: NavigatorViewModel = koinViewModel(),
    audioViewModel: AudioViewModel = koinViewModel(),
    mainScreenViewModel: MainScreenViewModel = koinViewModel(),
    playlistsViewModel: PlaylistsViewModel = koinViewModel(),
    playlistSelectionViewModel: PlaylistSelectionViewModel = koinViewModel(),
    playingScreenViewModel: PlayingScreenViewModel = koinViewModel(),
    songsViewModel: SongsViewModel = koinViewModel(),
    featuresViewModel: FeaturesViewModel = koinViewModel(),
    themeViewModel: ThemeViewModel = koinViewModel(),
    artistDetailScreenViewModel: ArtistDetailScreenViewModel = koinViewModel(),
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    val songsListState = rememberLazyListState()
    val albumsListState = rememberLazyListState()
    val foldersListState = rememberLazyListState()
    val artistsListState = rememberLazyListState()
    val playlistsListState = rememberLazyListState()

    val millis = 500

    if (startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(millis)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(millis)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(millis)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(millis)) }
        ) {
            mediaFoldersRoute(navController, mainScreenViewModel = mainScreenViewModel)
            homeRoute(navController, mainScreenViewModel = mainScreenViewModel, audioViewModel = audioViewModel)
            mainRoute(
                navController = navController,
                songListState = songsListState,
                albumListState = albumsListState,
                playlistsListState = playlistsListState,
                foldersListState = foldersListState,
                artistsListState = artistsListState,
                mainScreenViewModel = mainScreenViewModel,
                playlistsViewModel = playlistsViewModel,
                playlistSelectionViewModel = playlistSelectionViewModel,
                audioViewModel = audioViewModel,
                playingScreenViewModel = playingScreenViewModel
            )
            settingsRoute(navController)
            playingRoute(
                navController = navController,
                playingScreenViewModel = playingScreenViewModel,
                audioViewModel = audioViewModel,
                themeViewModel = themeViewModel
            )
            settingsTabsRoute(navController)

            albumDetailRoute(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel,
                audioViewModel = audioViewModel,
                playingScreenViewModel = playingScreenViewModel
            )

            songsRoute(
                navController = navController,
                audioViewModel = audioViewModel,
                mainScreenViewModel = mainScreenViewModel,
                listState = songsListState,
                featuresViewModel = featuresViewModel,
                playingScreenViewModel = playingScreenViewModel,
            )
            albumsRoute(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel,
                listState = albumsListState,
                audioViewModel = audioViewModel,
                playingScreenViewModel = playingScreenViewModel,
                featuresViewModel = featuresViewModel
            )
            artistsRoute(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel,
                listState = artistsListState,
                featuresViewModel = featuresViewModel,
                audioViewModel = audioViewModel,
                playingScreenViewModel = playingScreenViewModel
            )
            searchRoute(navController, mainScreenViewModel = mainScreenViewModel, audioViewModel = audioViewModel)
            artistDetailRoute(
                navController = navController,
                featuresViewModel = featuresViewModel,
                songsViewModel = songsViewModel,
                audioViewModel = audioViewModel,
                mainScreenViewModel = mainScreenViewModel,
                playingScreenViewModel = playingScreenViewModel,
                artistDetailScreenViewModel = artistDetailScreenViewModel
            )
            foldersRoute(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel,
                listState = foldersListState,
                audioViewModel = audioViewModel,
                featuresViewModel = featuresViewModel,
                playingScreenViewModel = playingScreenViewModel
            )
            folderSongsRoute(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel,
                songsViewModel = songsViewModel,
                audioViewModel = audioViewModel,
                featuresViewModel = featuresViewModel,
                playingScreenViewModel = playingScreenViewModel
            )
            playlistsRoute(
                navController = navController,
                listState = playlistsListState,
                mainScreenViewModel = mainScreenViewModel,
                playlistSelectionViewModel = playlistSelectionViewModel,
                playlistsViewModel = playlistsViewModel,
                audioViewModel = audioViewModel,
                featuresViewModel = featuresViewModel,
                playingScreenViewModel = playingScreenViewModel
            )

            playlistDetailRoute(
                navController = navController,
                mainViewModel = mainScreenViewModel,
                audioViewModel = audioViewModel,
                songsViewModel = songsViewModel,
                playingScreenViewModel = playingScreenViewModel
            )
        }
    } else {
        LoadingScreen()
    }
}

fun NavGraphBuilder.homeRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel
) {
    composable<HomeRoute> {
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()

        HomeScreen(
            loadingStatus = loadingStatus,
            navController = navController,
            spacerHeight = 0.dp,
            audioViewModel = audioViewModel
        )
    }
}

fun NavGraphBuilder.mainRoute(
    navController: NavHostController,
    songListState: LazyListState,
    albumListState: LazyListState,
    playlistsListState: LazyListState,
    foldersListState: LazyListState,
    artistsListState: LazyListState,
    mainScreenViewModel: MainScreenViewModel,
    playlistSelectionViewModel: PlaylistSelectionViewModel,
    playlistsViewModel: PlaylistsViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel
) {
    composable<MainRoute> {
        MainScreen(
            navController = navController,
            songsListState = songListState,
            albumsListState = albumListState,
            playlistsListState = playlistsListState,
            foldersListState = foldersListState,
            artistsListState = artistsListState,
            viewModel = mainScreenViewModel,
            playlistSelectionViewModel = playlistSelectionViewModel,
            playlistsViewModel = playlistsViewModel,
            audioViewModel = audioViewModel,
            playingScreenViewModel = playingScreenViewModel
        )
    }
}

fun NavGraphBuilder.mediaFoldersRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel
) {
    composable<MediaFoldersRoute> {
        MediaFoldersScreen(navController = navController, mainViewModel = mainScreenViewModel)
    }
}

fun NavGraphBuilder.settingsRoute(navController: NavHostController) {
    composable<SettingsRoute> {
        SettingsScreen(navController = navController)
    }
}

fun NavGraphBuilder.playingRoute(
    navController: NavHostController,
    playingScreenViewModel: PlayingScreenViewModel,
    audioViewModel: AudioViewModel,
    themeViewModel: ThemeViewModel
) {
    composable<PlayingRoute> {
        PlayingScreen(
            navController = navController,
            audioViewModel = audioViewModel,
            viewModel = playingScreenViewModel,
            themeViewModel = themeViewModel
        )
    }
}

fun NavGraphBuilder.settingsTabsRoute(
    navController: NavHostController,
) {
    composable<SettingsTabsRoute> {
        SettingsTabsScreen(navController = navController)
    }
}

fun NavGraphBuilder.albumDetailRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    composable<AlbumDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AlbumDetailRoute>()
        val songs by mainScreenViewModel.songs.collectAsStateWithLifecycle()
        val albums by mainScreenViewModel.albums.collectAsStateWithLifecycle()

        AlbumDetailScreen(
            navController = navController,
            albumId = route.albumId,
            songs = songs,
            albums = albums,
            audioViewModel = audioViewModel,
            playingScreenViewModel = playingScreenViewModel
        )
    }
}

fun NavGraphBuilder.playlistDetailRoute(
    navController: NavHostController,
    mainViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel,
    songsViewModel: SongsViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    composable<PlaylistDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PlaylistDetailRoute>()

        PlaylistDetailScreen(
            navController = navController,
            playlistId = route.playlistId,
            mainScreenViewModel = mainViewModel,
            audioViewModel = audioViewModel,
            songsViewModel = songsViewModel,
            playingScreenViewModel = playingScreenViewModel
        )
    }
}


fun NavGraphBuilder.songsRoute(
    navController: NavHostController,
    audioViewModel: AudioViewModel,
    mainScreenViewModel: MainScreenViewModel,
    listState: LazyListState,
    featuresViewModel: FeaturesViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    composable<SongsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SongsRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val songs by mainScreenViewModel.songs.collectAsStateWithLifecycle()
        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

        SongsScreen(
            navController = navController,
            listState = listState,
            loadingStatus = loadingStatus,
            spacerHeight = spacerHeight,
            songs = songs,
            fromOthers = route.fromOthers,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            playingScreenViewModel = playingScreenViewModel,
            onPlaySong = { playlistTitle, songs, song ->
                audioViewModel.setPlaylistName(playlistTitle)
                audioViewModel.loadPlaylist(songs, song)
                audioViewModel.playSong(song)
            },
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = audioViewModel::onPlayPauseClick
        )
    }
}

fun NavGraphBuilder.artistsRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    listState: LazyListState,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
    featuresViewModel: FeaturesViewModel,
) {
    composable<ArtistsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<ArtistsRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val artists by mainScreenViewModel.artists.collectAsStateWithLifecycle()
        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

        ArtistsScreen(
            navController = navController,
            listState = listState,
            loadingStatus = loadingStatus,
            spacerHeight = spacerHeight,
            artists = artists,
            fromOthers = route.fromOthers,
            playingScreenViewModel = playingScreenViewModel,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = audioViewModel::onPlayPauseClick
        )
    }
}

fun NavGraphBuilder.albumsRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    listState: LazyListState,
    playingScreenViewModel: PlayingScreenViewModel,
    audioViewModel: AudioViewModel,
    featuresViewModel: FeaturesViewModel,
) {
    composable<AlbumsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AlbumsRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val albums by mainScreenViewModel.albums.collectAsStateWithLifecycle()
        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

        AlbumsScreen(
            navController = navController,
            loadingStatus = loadingStatus,
            albums = albums,
            listState = listState,
            spacerHeight = spacerHeight,
            fromOthers = route.fromOthers,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            playingScreenViewModel = playingScreenViewModel,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = audioViewModel::onPlayPauseClick
        )
    }
}

fun NavGraphBuilder.playlistsRoute(
    navController: NavHostController,
    listState: LazyListState,
    mainScreenViewModel: MainScreenViewModel,
    playlistSelectionViewModel: PlaylistSelectionViewModel,
    playlistsViewModel: PlaylistsViewModel,
    featuresViewModel: FeaturesViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    composable<PlaylistsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PlaylistsRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val playlists by mainScreenViewModel.playlists.collectAsStateWithLifecycle()
        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

        PlaylistsScreen(
            navController = navController,
            loadingStatus = loadingStatus,
            playlists = playlists,
            spacerHeight = spacerHeight,
            fromOthers = route.fromOthers,
            listState = listState,
            onAddPlaylist = mainScreenViewModel::addPlaylistToCollection,
            onRenamePlaylist = mainScreenViewModel::renamePlaylist,
            playlistSelectionViewModel = playlistSelectionViewModel,
            viewModel = playlistsViewModel,
            playingScreenViewModel = playingScreenViewModel,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            mainViewModel = mainScreenViewModel,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = audioViewModel::onPlayPauseClick
        )
    }
}

fun NavGraphBuilder.foldersRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    listState: LazyListState,
    featuresViewModel: FeaturesViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    composable<FoldersRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<FoldersRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

        FoldersScreen(
            navController = navController,
            listState = listState,
            loadingStatus = loadingStatus,
            spacerHeight = spacerHeight,
            fromOthers = route.fromOthers,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            playingScreenViewModel = playingScreenViewModel,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = audioViewModel::onPlayPauseClick
        )
    }
}

fun NavGraphBuilder.searchRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel
) {
    composable<SearchRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AlbumsRoute>()
        val loadingStatus by mainScreenViewModel.loadingStatus.collectAsStateWithLifecycle()
        val query by mainScreenViewModel.query.collectAsStateWithLifecycle()

        val filteredSongs by mainScreenViewModel.filteredSongs.collectAsStateWithLifecycle()
        val filteredAlbums by mainScreenViewModel.filteredAlbums.collectAsStateWithLifecycle()
        val filteredArtists by mainScreenViewModel.filteredArtists.collectAsStateWithLifecycle()

        SearchScreen(
            navController = navController,
            fromOthers = route.fromOthers,
            filteredSongs = filteredSongs,
            filteredAlbums = filteredAlbums,
            filteredArtists = filteredArtists,
            onUpdateQuery = mainScreenViewModel::updateQuery,
            loadingStatus = loadingStatus,
            query = query,
            audioViewModel = audioViewModel
        )
    }
}

fun NavGraphBuilder.artistDetailRoute(
    navController: NavHostController,
    featuresViewModel: FeaturesViewModel,
    songsViewModel: SongsViewModel,
    audioViewModel: AudioViewModel,
    mainScreenViewModel: MainScreenViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
    artistDetailScreenViewModel: ArtistDetailScreenViewModel
) {
    composable<ArtistDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<ArtistDetailRoute>()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()

        ArtistDetailScreen(
            navController = navController,
            artistId = route.artistId,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            songsViewModel = songsViewModel,
            audioViewModel = audioViewModel,
            mainViewModel = mainScreenViewModel,
            playingScreenViewModel = playingScreenViewModel,
            viewModel = artistDetailScreenViewModel
        )
    }
}

fun NavGraphBuilder.folderSongsRoute(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel,
    featuresViewModel: FeaturesViewModel,
    songsViewModel: SongsViewModel,
    playingScreenViewModel: PlayingScreenViewModel
) {
    composable<FolderSongsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<FolderSongsRoute>()

        val spacerHeight by mainScreenViewModel.customBarHeight.collectAsStateWithLifecycle()
        val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()
        val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()
        val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()

        FoldersSongsScreen(
            navController = navController,
            spacerHeight = spacerHeight,
            folderPath = route.folderPath,
            selectedScreenFeatures = selectedScreenFeatures?.toSet(),
            songsViewModel = songsViewModel,
            mediaState = mediaState,
            playingScreenViewModel = playingScreenViewModel,
            onPlayPauseClick = audioViewModel::onPlayPauseClick,
            currentPlayingSong = currentPlayingSong,
            onPlaySong = { playlistTitle, songs, song ->
                audioViewModel.setPlaylistName(playlistTitle)
                audioViewModel.loadPlaylist(songs, song)
                audioViewModel.playSong(song)
            },
            mainViewModel = mainScreenViewModel
        )
    }
}