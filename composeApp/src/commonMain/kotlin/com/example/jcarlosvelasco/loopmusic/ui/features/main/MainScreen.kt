package com.example.jcarlosvelasco.loopmusic.ui.features.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.features.FeaturesViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection.PlaylistSelectionViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlists.PlaylistsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.search.SearchScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.components.AddToPlaylistPill
import com.example.jcarlosvelasco.loopmusic.ui.components.PlaylistSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.components.SongSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.features.albums.AlbumsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.artists.ArtistsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.folders.FoldersScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.home.HomeScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.other.OtherScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.playlists.PlaylistsScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.search.SearchScreen
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongsScreen
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel = koinViewModel(),
    playingScreenViewModel: PlayingScreenViewModel = koinViewModel(),
    featuresViewModel: FeaturesViewModel = koinViewModel(),
    songsListState: LazyListState,
    albumsListState: LazyListState,
    artistsListState: LazyListState,
    playlistsListState: LazyListState,
    foldersListState: LazyListState,
    searchScreenViewModel: SearchScreenViewModel = koinViewModel(),
    songsViewModel: SongsViewModel = koinViewModel(),
    playlistSelectionViewModel: PlaylistSelectionViewModel,
    playlistsViewModel: PlaylistsViewModel,
) {
    //TODO: ALL PILLS SHOULD BE CLOSED ON BACK PRESSED OR ANY OTHER NAVIGATION EVENT.

    val selectedTab by featuresViewModel.selectedTab.collectAsStateWithLifecycle()

    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()

    val loadingStatus by viewModel.loadingStatus.collectAsStateWithLifecycle()
    val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()
    val selectedScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()

    val playingPillHeight by playingScreenViewModel.playingPillHeight.collectAsStateWithLifecycle()
    val customNavigationBarHeight by viewModel.customBarHeight.collectAsStateWithLifecycle()

    val query by viewModel.query.collectAsStateWithLifecycle()

    val isTextFieldFocused by searchScreenViewModel.isTextFieldFocused.collectAsStateWithLifecycle()

    val density = LocalDensity.current

    val isSelectionMode by songsViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val isPlaylistSelectionMode by songsViewModel.isPlaylistSelectionMode.collectAsStateWithLifecycle()
    val isPSelectionMode by playlistSelectionViewModel.isSelectionMode.collectAsStateWithLifecycle()

    val spacerHeight = playingPillHeight + customNavigationBarHeight

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(top = 16.dp)
        ) {
            when (selectedTab) {
                NavigationTab.HOME -> HomeScreen(
                    navController,
                    loadingStatus,
                    spacerHeight = spacerHeight
                )

                NavigationTab.SONGS -> SongsScreen(
                    navController = navController,
                    songs,
                    songsListState,
                    loadingStatus,
                    spacerHeight = spacerHeight,
                    selectedScreenFeatures = selectedScreenFeatures?.toSet(),
                    playingScreenViewModel = playingScreenViewModel,
                    currentPlayingSong = currentPlayingSong,
                    mediaState = mediaState,
                    onPlayPauseClick = audioViewModel::onPlayPauseClick,
                    onPlaySong = { playlistTitle, songs, song ->
                        audioViewModel.setPlaylistName(playlistTitle)
                        audioViewModel.loadPlaylist(songs, song)
                        audioViewModel.playSong(song)
                    }
                )

                NavigationTab.OTHER -> OtherScreen(
                    remainingScreenFeatures = featuresViewModel.remainingScreenFeatures?.toList(),
                    navController = navController
                )

                NavigationTab.ALBUMS -> AlbumsScreen(
                    navController = navController,
                    loadingStatus = loadingStatus,
                    albums = albums,
                    listState = albumsListState,
                    spacerHeight = spacerHeight,
                    selectedScreenFeatures = selectedScreenFeatures?.toSet(),
                    playingScreenViewModel = playingScreenViewModel,
                    currentPlayingSong = currentPlayingSong,
                    mediaState = mediaState,
                    onPlayPauseClick = audioViewModel::onPlayPauseClick
                )

                NavigationTab.SEARCH -> SearchScreen(
                    navController = navController,
                    loadingStatus = loadingStatus,
                    onUpdateQuery = viewModel::updateQuery,
                    query = query,
                    filteredSongs = viewModel.filteredSongs,
                    filteredAlbums = viewModel.filteredAlbums,
                    filteredArtists = viewModel.filteredArtists,
                )

                NavigationTab.ARTISTS -> ArtistsScreen(
                    navController = navController,
                    loadingStatus = loadingStatus,
                    listState = artistsListState,
                    spacerHeight = spacerHeight,
                    artists = artists,
                    currentPlayingSong = currentPlayingSong,
                    mediaState = mediaState,
                    onPlayPauseClick = audioViewModel::onPlayPauseClick,
                    selectedScreenFeatures = selectedScreenFeatures?.toSet(),
                    playingScreenViewModel = playingScreenViewModel
                )

                NavigationTab.FOLDERS -> FoldersScreen(
                    navController = navController,
                    listState = foldersListState,
                    loadingStatus = loadingStatus,
                    spacerHeight = spacerHeight,
                    currentPlayingSong = currentPlayingSong,
                    mediaState = mediaState,
                    onPlayPauseClick = audioViewModel::onPlayPauseClick,
                    playingScreenViewModel = playingScreenViewModel,
                    selectedScreenFeatures = selectedScreenFeatures?.toSet()
                )

                NavigationTab.PLAYLISTS -> PlaylistsScreen(
                    navController = navController,
                    listState = playlistsListState,
                    loadingStatus = loadingStatus,
                    playlists = playlists,
                    spacerHeight = spacerHeight,
                    onAddPlaylist = viewModel::addPlaylistToCollection,
                    onRenamePlaylist = viewModel::renamePlaylist,
                    viewModel = playlistsViewModel,
                    playlistSelectionViewModel = playlistSelectionViewModel,
                    currentPlayingSong = currentPlayingSong,
                    mediaState = mediaState,
                    onPlayPauseClick = audioViewModel::onPlayPauseClick,
                    playingScreenViewModel = playingScreenViewModel,
                    selectedScreenFeatures = selectedScreenFeatures?.toSet(),
                    mainViewModel = viewModel
                )

                null -> Text("Error")
            }

            AnimatedVisibility(
                visible = isSelectionMode && !isPlaylistSelectionMode,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                SongSelectionPill()
            }

            AnimatedVisibility(
                visible = isPlaylistSelectionMode,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AddToPlaylistPill()
            }

            AnimatedVisibility(
                visible = isPSelectionMode,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                PlaylistSelectionPill(
                    viewModel = playlistSelectionViewModel,
                    playlistScreenViewModel = playlistsViewModel
                )
            }

            AnimatedVisibility(
                visible = !isTextFieldFocused && !isSelectionMode && !isPSelectionMode,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column {
                    currentPlayingSong?.let { song ->
                        PlayingPill(
                            mediaState = mediaState,
                            song = song,
                            onClick = { safeNavigate(navController, PlayingRoute) },
                            onPlayPauseClick = { audioViewModel.onPlayPauseClick() },
                            modifier = Modifier.onGloballyPositioned {
                                playingScreenViewModel.setPlayingPillHeight(with(density) {
                                    it.size.height.toDp()
                                })
                            }
                        )
                    }

                    selectedTab?.let {
                        CustomBottomNavigationBar(
                            selectedTab = it,
                            onTabSelected = { featuresViewModel.setNavigationTab(it) },
                            modifier = Modifier.onGloballyPositioned {
                                viewModel.setCustomBarHeight(with(density) {
                                    it.size.height.toDp()
                                })
                            },
                            navigationTabs = featuresViewModel.selectedNavigationTabs
                        )
                    }
                }
            }
        }
    }
}