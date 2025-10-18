package com.example.jcarlosvelasco.loopmusic.ui.features.artist_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.presentation.artist_detail.ArtistDetailScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.WithOrientation
import com.example.jcarlosvelasco.loopmusic.ui.components.AddToPlaylistPill
import com.example.jcarlosvelasco.loopmusic.ui.components.SongSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.features.main.PlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.ArtistAlbumsInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.ArtistInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.ArtistSongsInfoSkeleton
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.FeaturedArtistSongsSkeleton
import com.example.jcarlosvelasco.loopmusic.utils.let2
import com.example.jcarlosvelasco.loopmusic.utils.log
import com.kmpalette.extensions.bytearray.rememberByteArrayDominantColorState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArtistDetailScreen(
    artistId: Long,
    navController: NavHostController,
    viewModel: ArtistDetailScreenViewModel,
    mainViewModel: MainScreenViewModel,
    audioViewModel: AudioViewModel,
    playingScreenViewModel: PlayingScreenViewModel,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    songsViewModel: SongsViewModel
) {
    val allArtists by mainViewModel.artists.collectAsStateWithLifecycle()
    val allSongs by mainViewModel.songs.collectAsStateWithLifecycle()

    val featuredSongs by viewModel.featuredSongs.collectAsStateWithLifecycle()
    val artist by viewModel.artist.collectAsStateWithLifecycle()
    val artistSongs by viewModel.artistSongs.collectAsStateWithLifecycle()
    val albums by viewModel.artistAlbums.collectAsStateWithLifecycle()

    val dominantColor by viewModel.dominantColor.collectAsStateWithLifecycle()
    val dominantOnColor by viewModel.dominantOnColor.collectAsStateWithLifecycle()

    val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val playingPillHeight: Dp by playingScreenViewModel.playingPillHeight.collectAsStateWithLifecycle()

    val dominantColorState = rememberByteArrayDominantColorState(
        defaultColor = dominantColor ?: MaterialTheme.colorScheme.background,
        defaultOnColor = dominantOnColor ?: MaterialTheme.colorScheme.onBackground
    )

    val isSelectionMode by songsViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val isPlaylistSelectionMode by songsViewModel.isPlaylistSelectionMode.collectAsStateWithLifecycle()

    BackHandler {
        songsViewModel.setIsPlaylistSelectionMode(false)
        songsViewModel.updateSelectionMode(false)
        safePopBackStack(navController)
    }

    LaunchedEffect(artistId) {
        log("ArtistDetailScreen", "Loading artist detail for id: $artistId")
        if (viewModel.artistID != artistId) {
            viewModel.updateArtistID(artistId)

            let2(allArtists, allSongs) { artists, songs ->
                viewModel.loadAlbumFromMemory(artistId, artists, songs)
                artist?.artwork?.let {
                    dominantColorState.updateFrom(it)
                    viewModel.updateDominantColors(
                        dominantColorState.color,
                        dominantColorState.onColor
                    )
                }
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
                        onClick = {
                            songsViewModel.setIsPlaylistSelectionMode(false)
                            songsViewModel.updateSelectionMode(false)
                            safePopBackStack(navController)
                        }
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
                                    let2(artist, artistSongs) { a, s ->
                                        albums?.let {
                                            ArtistInfo(
                                                a,
                                                s,
                                                it,
                                                isLandscape,
                                                onPlayClick = {
                                                    audioViewModel.setPlaylistName(a.name)
                                                    featuredSongs?.let { f ->
                                                        audioViewModel.loadPlaylistAndPlay(
                                                            playlist = s.plus(f),
                                                            isShuffled = false
                                                        )
                                                    }
                                                },
                                                onShuffleClick = {
                                                    audioViewModel.setPlaylistName(a.name)
                                                    featuredSongs?.let { f ->
                                                        audioViewModel.loadPlaylistAndPlay(
                                                            playlist = s.plus(f),
                                                            isShuffled = true
                                                        )
                                                    }
                                                },
                                                dominantColor = dominantColor,
                                                onDominantColor = dominantOnColor
                                            )
                                        }
                                    } ?: ArtistInfoSkeleton(isLandscape)

                                    Column {
                                        albums?.let {
                                            ArtistAlbumsInfo(albums = it, navController = navController)
                                        } ?: ArtistAlbumsInfoSkeleton()

                                        let2(artist, artistSongs) { a, s ->
                                            ArtistSongsInfo(
                                                artistName = a.name,
                                                albumSongs = s,
                                                navController = navController,
                                                viewModel = songsViewModel,
                                                onPlaySong = { name, songs, selectedSong ->
                                                    audioViewModel.setPlaylistName(name)
                                                    audioViewModel.loadPlaylist(songs, selectedSong)
                                                    audioViewModel.playSong(selectedSong)
                                                }
                                            )
                                        } ?: ArtistSongsInfoSkeleton()

                                        let2(featuredSongs, artist) { f, a ->
                                            if (f.isEmpty()) return@let2
                                            FeaturedArtistSongs(
                                                featuredSongs = f,
                                                artist = a,
                                                navController = navController,
                                                viewModel = songsViewModel,
                                                onPlaySong = { name, songs, selectedSong ->
                                                    audioViewModel.setPlaylistName(name)
                                                    audioViewModel.loadPlaylist(songs, selectedSong)
                                                    audioViewModel.playSong(selectedSong)
                                                }
                                            )
                                        } ?: FeaturedArtistSongsSkeleton()
                                    }
                                }
                            }
                        } else {
                            item {
                                let2(artist, artistSongs) { a, s ->
                                    albums?.let {
                                        ArtistInfo(
                                            a,
                                            s,
                                            it,
                                            isLandscape,
                                            onPlayClick = {
                                                audioViewModel.setPlaylistName(a.name)
                                                featuredSongs?.let { f ->
                                                    audioViewModel.loadPlaylistAndPlay(
                                                        playlist = s.plus(f),
                                                        isShuffled = false
                                                    )
                                                }
                                            },
                                            onShuffleClick = {
                                                audioViewModel.setPlaylistName(a.name)
                                                featuredSongs?.let { f ->
                                                    audioViewModel.loadPlaylistAndPlay(
                                                        playlist = s.plus(f),
                                                        isShuffled = true
                                                    )
                                                }
                                            },
                                            dominantColor = dominantColor,
                                            onDominantColor = dominantOnColor
                                        )
                                    }
                                } ?: ArtistInfoSkeleton(isLandscape)

                                Spacer(modifier = Modifier.height(32.dp))

                                albums?.let {
                                    ArtistAlbumsInfo(albums = it, navController = navController)
                                } ?: ArtistAlbumsInfoSkeleton()

                                Spacer(modifier = Modifier.height(16.dp))

                                let2(artist, artistSongs) { a, s ->
                                    ArtistSongsInfo(
                                        artistName = a.name,
                                        albumSongs = s,
                                        navController = navController,
                                        viewModel = songsViewModel,
                                        onPlaySong = { name, songs, selectedSong ->
                                            audioViewModel.setPlaylistName(name)
                                            audioViewModel.loadPlaylist(songs, selectedSong)
                                            audioViewModel.playSong(selectedSong)
                                        }
                                    )
                                } ?: ArtistSongsInfoSkeleton()

                                Spacer(modifier = Modifier.height(16.dp))

                                let2(featuredSongs, artist) { f, a ->
                                    if (f.isEmpty()) return@let2
                                    FeaturedArtistSongs(
                                        featuredSongs = f,
                                        artist = a,
                                        navController = navController,
                                        viewModel = songsViewModel,
                                        onPlaySong = { name, songs, selectedSong ->
                                            audioViewModel.setPlaylistName(name)
                                            audioViewModel.loadPlaylist(songs, selectedSong)
                                            audioViewModel.playSong(selectedSong)
                                        }
                                    )
                                } ?: FeaturedArtistSongsSkeleton()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(playingPillHeight))
                        }
                    }
                }

                AnimatedVisibility(
                    visible = currentPlayingSong != null && !isSelectionMode && !isPlaylistSelectionMode,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    currentPlayingSong?.let { song ->
                        PlayingPill(
                            mediaState = mediaState,
                            song = song,
                            onClick = { safeNavigate(navController, PlayingRoute) },
                            onPlayPauseClick = { audioViewModel.onPlayPauseClick() },
                            modifier = Modifier
                                .onGloballyPositioned {
                                    playingScreenViewModel.setPlayingPillHeight(
                                        with(density) {
                                            it.size.height.toDp()
                                        }
                                    )
                                }
                        )
                    }
                }

                selectedScreenFeatures?.let { features ->
                    AnimatedVisibility(
                        visible = isSelectionMode && !isPlaylistSelectionMode && !features.contains(SCREEN_FEATURES.Songs),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        SongSelectionPill()
                    }

                    AnimatedVisibility(
                        visible = isPlaylistSelectionMode && !features.contains(SCREEN_FEATURES.Songs),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        AddToPlaylistPill()
                    }
                }
            }
        }
    }
}