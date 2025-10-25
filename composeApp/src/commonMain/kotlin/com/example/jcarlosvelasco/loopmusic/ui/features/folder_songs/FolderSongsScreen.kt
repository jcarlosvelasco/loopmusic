package com.example.jcarlosvelasco.loopmusic.ui.features.folder_songs

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.folder_songs.FolderSongsViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.components.AddToPlaylistPill
import com.example.jcarlosvelasco.loopmusic.ui.components.ScreenWithPlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.components.SongSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.songs_header
import loopmusic.composeapp.generated.resources.songs_no_songs
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FoldersSongsScreen(
    folderPath: String,
    navController: NavHostController,
    viewModel: FolderSongsViewModel = koinViewModel(),
    spacerHeight: Dp,
    mainViewModel: MainScreenViewModel,
    songsViewModel: SongsViewModel,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    onPlaySong: (String, List<Song>, Song) -> Unit,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit
) {
    val folder by viewModel.folder.collectAsStateWithLifecycle()
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val allSongs by mainViewModel.songs.collectAsStateWithLifecycle()

    val isSelectionMode by songsViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedSongs by songsViewModel.selectedSongs.collectAsStateWithLifecycle()
    val isPlaylistSelectionMode by songsViewModel.isPlaylistSelectionMode.collectAsStateWithLifecycle()

    BackHandler {
        songsViewModel.setIsPlaylistSelectionMode(false)
        songsViewModel.updateSelectionMode(false)
        safePopBackStack(navController)
    }

    LaunchedEffect(folderPath) {
        if (viewModel.folderPath != folderPath) {
            allSongs?.let { songs ->
                viewModel.updateFolderPath(folderPath, songs)
            }
        }
    }

    Scaffold {
        ScreenWithPlayingPill(
            navController = navController,
            selectedScreenFeatures = selectedScreenFeatures,
            playingScreenViewModel = playingScreenViewModel,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(top = 16.dp),
            selectedFeature = SCREEN_FEATURES.Songs,
            condition = !isSelectionMode,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = onPlayPauseClick
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier

                        .fillMaxSize()
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
                    Spacer(modifier = Modifier.padding(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            stringResource(Res.string.songs_header),
                            style = appTypography().headlineLarge
                        )
                    }

                    Spacer(modifier = Modifier.padding(12.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            songs == null -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .wrapContentHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            songs!!.isEmpty() -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .wrapContentHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "📁",
                                                style = appTypography().headlineLarge,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                            Text(
                                                stringResource(Res.string.songs_no_songs),
                                                style = appTypography().bodyMedium
                                            )
                                        }
                                    }
                                }
                            }

                            else -> {
                                for (song in songs) {
                                    item {
                                        SongItem(
                                            song,
                                            isSelected = selectedSongs.contains(song),
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            onClick = {
                                                if (isSelectionMode) {
                                                    if (songsViewModel.isSongSelected(song)) {
                                                        songsViewModel.removeSongFromSelected(song)
                                                    } else {
                                                        songsViewModel.addSongToSelected(song)
                                                    }
                                                } else {
                                                    folder?.let {
                                                        onPlaySong(it.name, songs!!, song)
                                                    }
                                                    safeNavigate(
                                                        navController,
                                                        PlayingRoute,
                                                    )
                                                }
                                            },
                                            isSelectionMode = isSelectionMode,
                                            onLongClick = {
                                                if (!isSelectionMode) {
                                                    songsViewModel.updateSelectionMode(true)
                                                    songsViewModel.addSongToSelected(song)
                                                } else {
                                                    if (songsViewModel.isSongSelected(song)) {
                                                        songsViewModel.removeSongFromSelected(song)
                                                    } else {
                                                        songsViewModel.addSongToSelected(song)
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(spacerHeight))
                                }
                            }
                        }
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