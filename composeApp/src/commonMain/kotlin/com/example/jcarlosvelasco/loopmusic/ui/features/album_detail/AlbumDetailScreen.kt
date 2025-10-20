package com.example.jcarlosvelasco.loopmusic.ui.features.album_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.example.jcarlosvelasco.loopmusic.presentation.album_detail.AlbumDetailScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.WithOrientation
import com.example.jcarlosvelasco.loopmusic.ui.features.main.PlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.AlbumInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.AlbumSongsInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.utils.let2
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlbumDetailScreen(
    albumId: Long,
    navController: NavHostController,
    viewModel: AlbumDetailScreenViewModel = koinViewModel(),
    mainViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
) {
    val songs by mainViewModel.songs.collectAsStateWithLifecycle()
    val albums by mainViewModel.albums.collectAsStateWithLifecycle()

    val album by viewModel.album.collectAsStateWithLifecycle()
    val albumSongs by viewModel.songs.collectAsStateWithLifecycle()

    val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val playingPillHeight: Dp by playingScreenViewModel.playingPillHeight.collectAsStateWithLifecycle()

    val fullQualityArtwork by viewModel.fullQualityArtwork.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        if (viewModel.albumID != albumId) {
            viewModel.updateAlbumID(albumId)
            let2(songs, albums) { songs, albums ->
                viewModel.loadAlbumFromMemory(albumId, songs, albums)
            }
        }
    }

    WithOrientation { isLandscape ->
        Scaffold {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(
                        onClick = { safePopBackStack(navController) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }

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
                                    album?.let {
                                        AlbumInfo(
                                            it,
                                            albumSongs,
                                            isLandscape,
                                            fullQualityArtwork,
                                            onPlayClick = {
                                                audioViewModel.setPlaylistName(it.name)
                                                audioViewModel.loadPlaylistAndPlay(
                                                    playlist = albumSongs,
                                                    isShuffled = false
                                                )
                                            },
                                            onShuffleClick = {
                                                audioViewModel.setPlaylistName(it.name)
                                                audioViewModel.loadPlaylistAndPlay(
                                                    playlist = albumSongs,
                                                    isShuffled = true
                                                )
                                            }
                                        )
                                    } ?: AlbumInfoSkeleton(isLandscape)

                                    album?.let {
                                        AlbumSongsInfo(it, albumSongs, audioViewModel, navController)
                                    } ?: AlbumSongsInfoSkeleton()
                                }
                            }
                        } else {
                            item {
                                album?.let {
                                    AlbumInfo(
                                        it,
                                        albumSongs,
                                        isLandscape,
                                        fullQualityArtwork,
                                        onPlayClick = {
                                            audioViewModel.setPlaylistName(it.name)
                                            audioViewModel.loadPlaylistAndPlay(
                                                playlist = albumSongs,
                                                isShuffled = false
                                            )
                                        },
                                        onShuffleClick = {
                                            audioViewModel.setPlaylistName(it.name)
                                            audioViewModel.loadPlaylistAndPlay(
                                                playlist = albumSongs,
                                                isShuffled = true
                                            )
                                        }
                                    )
                                } ?: AlbumInfoSkeleton(isLandscape)

                                Spacer(modifier = Modifier.height(32.dp))

                                album?.let {
                                    AlbumSongsInfo(it, albumSongs, audioViewModel, navController)
                                } ?: AlbumSongsInfoSkeleton()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(playingPillHeight))
                        }
                    }
                }

                currentPlayingSong?.let { song ->
                    PlayingPill(
                        mediaState = mediaState,
                        song = song,
                        onClick = { safeNavigate(navController, PlayingRoute) },
                        onPlayPauseClick = { audioViewModel.onPlayPauseClick() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .onGloballyPositioned {
                                playingScreenViewModel.setPlayingPillHeight(with(density) {
                                    it.size.height.toDp()
                                }
                                )
                            }
                    )
                }
            }
        }
    }
}