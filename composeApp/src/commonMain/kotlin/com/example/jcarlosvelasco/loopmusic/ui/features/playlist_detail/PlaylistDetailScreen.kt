package com.example.jcarlosvelasco.loopmusic.ui.features.playlist_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_detail.PlaylistDetailViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.WithOrientation
import com.example.jcarlosvelasco.loopmusic.ui.components.SongSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.features.main.PlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.PlaylistInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.utils.let2
import com.example.jcarlosvelasco.loopmusic.utils.log
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    navController: NavHostController,
    playlistId: Long,
    viewModel: PlaylistDetailViewModel = koinViewModel(),
    audioViewModel: AudioViewModel,
    mainScreenViewModel: MainScreenViewModel,
    songsViewModel: SongsViewModel,
    playingScreenViewModel: PlayingScreenViewModel
) {
    val playlist by viewModel.playlist.collectAsStateWithLifecycle()

    val allSongs by mainScreenViewModel.songs.collectAsStateWithLifecycle()

    val isSelectionMode by songsViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedSongs by songsViewModel.selectedSongs.collectAsStateWithLifecycle()

    val playlistSongs = if (playlist == null) null else allSongs?.filter { playlist?.songPaths?.contains(it.path) == true }?.toMutableList()

    val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()
    val playlistArtwork by viewModel.playlistArtwork.collectAsStateWithLifecycle()
    val playingPillHeight: Dp by playingScreenViewModel.playingPillHeight.collectAsStateWithLifecycle()

    val density = LocalDensity.current

    LaunchedEffect(playlistId) {
        if (playlistId != viewModel.playlistId) {
            log("PlaylistDetailScreen", "Loading playlist detail for id: $playlistId")
            viewModel.setPlaylistId(playlistId)
        }
    }

    LaunchedEffect(playlistSongs) {
        playlistSongs?.let { songs ->
            viewModel.loadPlaylistArtwork(songs)
        }
    }

    WithOrientation { isLandscape ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = { safePopBackStack(navController) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                        navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                        titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
                        actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
                        subtitleContentColor = TopAppBarDefaults.topAppBarColors().subtitleContentColor
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (isLandscape) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                                ) {
                                    let2(playlist, playlistSongs) { pla, songs ->
                                        PlaylistInfo(
                                            playlist = pla,
                                            songs = songs,
                                            artworkList = playlistArtwork,
                                            isLandscape = isLandscape,
                                            onPlayClick = {
                                                audioViewModel.setPlaylistName(pla.name)
                                                audioViewModel.loadPlaylistAndPlay(
                                                    playlist = songs.toList(),
                                                    isShuffled = false
                                                )
                                            },
                                            onShuffleClick = {
                                                audioViewModel.setPlaylistName(pla.name)
                                                audioViewModel.loadPlaylistAndPlay(
                                                    playlist = songs.toList(),
                                                    isShuffled = true
                                                )
                                            },
                                        )
                                    } ?: PlaylistInfoSkeleton(isLandscape)

                                    playlistSongs?.let { songs ->
                                        PlaylistSongs(
                                            playlistSongs = songs,
                                            selectedSongs = selectedSongs,
                                            isSelectionMode = isSelectionMode,
                                            songsViewModel = songsViewModel,
                                            playlist = playlist,
                                            audioViewModel = audioViewModel,
                                            navController = navController,
                                        )
                                    } ?: PlaylistSongsSkeleton()
                                }
                            }
                        } else {
                            item {
                                let2(playlist, playlistSongs) { pla, songs ->
                                    PlaylistInfo(
                                        playlist = pla,
                                        songs = songs,
                                        artworkList = playlistArtwork,
                                        isLandscape = isLandscape,
                                        onPlayClick = {
                                            audioViewModel.setPlaylistName(pla.name)
                                            audioViewModel.loadPlaylistAndPlay(
                                                playlist = songs.toList(),
                                                isShuffled = false
                                            )
                                        },
                                        onShuffleClick = {
                                            audioViewModel.setPlaylistName(pla.name)
                                            audioViewModel.loadPlaylistAndPlay(
                                                playlist = songs.toList(),
                                                isShuffled = true
                                            )
                                        },
                                    )
                                } ?: PlaylistInfoSkeleton(isLandscape)

                                Spacer(modifier = Modifier.height(32.dp))

                                playlistSongs?.let { songs ->
                                    PlaylistSongs(
                                        playlistSongs = songs,
                                        selectedSongs = selectedSongs,
                                        isSelectionMode = isSelectionMode,
                                        songsViewModel = songsViewModel,
                                        playlist = playlist,
                                        audioViewModel = audioViewModel,
                                        navController = navController,
                                    )
                                } ?: PlaylistSongsSkeleton()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(playingPillHeight))
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isSelectionMode,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                ) {
                    SongSelectionPill(
                        songsViewModel = songsViewModel,
                        allowRemoveFromPlaylist = true,
                        fromPlaylist = playlist,
                        onRemoveFromPlaylist = { songs, playlist ->
                            viewModel.removeSongsFromPlaylist(songs)
                            mainScreenViewModel.removeSongsFromPlaylist(songs, playlist)
                        }
                    )
                }

                AnimatedVisibility(
                    visible = !isSelectionMode && currentPlayingSong != null,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                ) {
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
                }
            }
        }
    }
}