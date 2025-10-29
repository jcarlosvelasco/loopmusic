package com.example.jcarlosvelasco.loopmusic.ui.features.playing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.PlatformBox
import com.example.jcarlosvelasco.loopmusic.ui.WithOrientation
import com.example.jcarlosvelasco.loopmusic.ui.navigation.AlbumDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.ArtistDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import com.example.jcarlosvelasco.loopmusic.ui.theme.urbanistFontFamily
import com.example.jcarlosvelasco.loopmusic.utils.log
import com.kmpalette.extensions.bytearray.rememberByteArrayDominantColorState
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.playing_gotoalbum
import loopmusic.composeapp.generated.resources.playing_gotoartist
import loopmusic.composeapp.generated.resources.playing_playing_from
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayingScreen(
    navController: NavHostController,
    audioViewModel: AudioViewModel,
    viewModel: PlayingScreenViewModel,
    themeViewModel: ThemeViewModel,
) {
    val currentPlayingSong by audioViewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val previousCurrentPlayingAlbum by audioViewModel.previousCurrentPlayingSongAlbum.collectAsStateWithLifecycle()

    val mediaState by audioViewModel.mediaState.collectAsStateWithLifecycle()
    val currentPosition by audioViewModel.currentPosition.collectAsStateWithLifecycle()
    val listMode by audioViewModel.listMode.collectAsStateWithLifecycle()
    val playMode by audioViewModel.playMode.collectAsStateWithLifecycle()
    val fullQualityArtwork by viewModel.fullQualityArtwork.collectAsStateWithLifecycle()

    val playlistName by audioViewModel.playlistName.collectAsStateWithLifecycle()

    val dominantColor by viewModel.dominantColor.collectAsStateWithLifecycle()
    val dominantOnColor by viewModel.dominantOnColor.collectAsStateWithLifecycle()

    val isMenuExpanded by viewModel.isMenuExpandedFlow.collectAsStateWithLifecycle()

    val isExternal by viewModel.isExternalPath.collectAsStateWithLifecycle()

    val dominantColorState = rememberByteArrayDominantColorState(
        defaultColor = dominantColor ?: MaterialTheme.colorScheme.background,
        defaultOnColor = dominantOnColor ?: MaterialTheme.colorScheme.onBackground
    )

    LaunchedEffect(currentPlayingSong) {
        currentPlayingSong?.let { currentSong ->
            log("PlayingScreen", "Previous current playing album: ${previousCurrentPlayingAlbum?.name}")
            if (previousCurrentPlayingAlbum == null) {
                if (viewModel.lastProcessedAlbum?.name != currentSong.album.name ) {
                    log("PlayingScreen", "First time playing song")
                    viewModel.getFullQualityArtwork(currentSong.path)
                    currentSong.album.artwork?.let {
                        dominantColorState.updateFrom(it)
                        viewModel.updateDominantColors(
                            dominantColorState.color,
                            dominantColorState.onColor
                        )
                    }
                    viewModel.updateLastProcessedAlbum(currentSong.album)
                }
            }
            else {
                log("PlayingScreen", "Previous current playing album: ${previousCurrentPlayingAlbum?.name}")
                if (
                    previousCurrentPlayingAlbum?.name != currentSong.album.name ||
                    previousCurrentPlayingAlbum?.artist?.name != currentSong.album.artist.name
                ) {
                    if (viewModel.lastProcessedAlbum?.name != currentSong.album.name) {
                        // New song is from different album
                        log("PlayingScreen", "New song is from different album")
                        viewModel.getFullQualityArtwork(currentSong.path)
                        currentSong.album.artwork?.let {
                            dominantColorState.updateFrom(it)
                            viewModel.updateDominantColors(
                                dominantColorState.color,
                                dominantColorState.onColor
                            )
                        }
                        viewModel.updateLastProcessedAlbum(currentSong.album)
                    }
                }
            }
            viewModel.updateExternalPath(currentSong.path)
        }
    }

    PlatformBox {
        WithOrientation { isLandscape ->
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeContentPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { safePopBackStack(navController) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Previous"
                            )
                        }

                        currentPlayingSong?.let {
                            if (!isExternal) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        stringResource(Res.string.playing_playing_from),
                                        style = appTypography().bodyMedium
                                    )
                                    Text(
                                        playlistName,
                                        fontSize = 16.sp,
                                        fontFamily = urbanistFontFamily(),
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Box {
                                    IconButton(onClick = { viewModel.updateIsMenuExpanded(!isMenuExpanded) }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                                    }
                                    DropdownMenu(
                                        expanded = isMenuExpanded,
                                        onDismissRequest = { viewModel.updateIsMenuExpanded(false) },
                                        containerColor = MaterialTheme.colorScheme.background
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    stringResource(Res.string.playing_gotoalbum),
                                                    style = appTypography().bodyLarge
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateIsMenuExpanded(false)

                                                safeNavigate(
                                                    navController = navController,
                                                    AlbumDetailRoute(it.album.id)
                                                )
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    stringResource(Res.string.playing_gotoartist),
                                                    style = appTypography().bodyLarge
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateIsMenuExpanded(false)
                                                log(
                                                    "PlayingScreen",
                                                    "Goto artist with id: ${currentPlayingSong?.album?.artist?.id}"
                                                )

                                                safeNavigate(
                                                    navController = navController,
                                                    ArtistDetailRoute(it.album.artist.id)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isLandscape) {
                        currentPlayingSong?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                PlayingScreenImage(
                                    currentPlayingSong = it,
                                    fullQualityArtwork = fullQualityArtwork
                                )

                                PlayingInfo(
                                    song = it,
                                    currentPosition = currentPosition,
                                    audioViewModel = audioViewModel,
                                    dominantColorState = dominantColorState,
                                    viewModel = viewModel,
                                    listMode = listMode,
                                    mediaState = mediaState,
                                    playMode = playMode,
                                    themeViewModel = themeViewModel
                                )
                            }
                        }
                    } else {
                        currentPlayingSong?.let {
                            PlayingScreenImage(
                                currentPlayingSong = it,
                                fullQualityArtwork = fullQualityArtwork
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            PlayingInfo(
                                song = it,
                                currentPosition = currentPosition,
                                audioViewModel = audioViewModel,
                                dominantColorState = dominantColorState,
                                viewModel = viewModel,
                                listMode = listMode,
                                mediaState = mediaState,
                                playMode = playMode,
                                themeViewModel = themeViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}