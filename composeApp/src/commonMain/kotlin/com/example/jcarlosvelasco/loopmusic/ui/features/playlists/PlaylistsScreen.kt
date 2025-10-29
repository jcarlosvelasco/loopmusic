package com.example.jcarlosvelasco.loopmusic.ui.features.playlists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.SongsLoadingStatus
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection.PlaylistSelectionViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlists.PlaylistsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.components.AlertDialogWithTextField
import com.example.jcarlosvelasco.loopmusic.ui.components.AlertDialog
import com.example.jcarlosvelasco.loopmusic.ui.components.PlaylistSelectionPill
import com.example.jcarlosvelasco.loopmusic.ui.components.ScreenWithPlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlaylistDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.launch
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlaylistsScreen(
    navController: NavHostController,
    playlists: List<Playlist>?,
    listState: LazyListState,
    loadingStatus: SongsLoadingStatus?,
    viewModel: PlaylistsViewModel,
    spacerHeight: Dp,
    fromOthers: Boolean = false,
    onRenamePlaylist: (Long, String) -> Unit,
    onAddPlaylist: (Playlist) -> Unit,
    playlistSelectionViewModel: PlaylistSelectionViewModel,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    mainViewModel: MainScreenViewModel,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit
) {
    val isCreatePlaylistModalOpen by viewModel.isCreatePlaylistModalOpen.collectAsStateWithLifecycle()
    val isSelectionMode by playlistSelectionViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedPlaylists by playlistSelectionViewModel.selectedPlaylists.collectAsStateWithLifecycle()
    val isRenameOptionSelected by playlistSelectionViewModel.isRenameOptionSelected.collectAsStateWithLifecycle()
    val isRemovePlaylistModalOpen by viewModel.isRemovePlaylistModalOpen.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    if (fromOthers) {
        BackHandler {
            playlistSelectionViewModel.updateSelectionMode(false)
            playlistSelectionViewModel.updateRenameOptionSelected(false)
            viewModel.updateCreatePlaylistModal(false)
            safePopBackStack(navController)
        }
    }

    Scaffold {
        ScreenWithPlayingPill(
            navController = navController,
            selectedScreenFeatures = selectedScreenFeatures,
            playingScreenViewModel = playingScreenViewModel,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            selectedFeature = SCREEN_FEATURES.Playlists,
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
                    if (fromOthers) {
                        IconButton(
                            onClick = {
                                playlistSelectionViewModel.updateSelectionMode(false)
                                playlistSelectionViewModel.updateRenameOptionSelected(false)
                                viewModel.updateCreatePlaylistModal(false)
                                safePopBackStack(navController)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                        Spacer(modifier = Modifier.padding(12.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            stringResource(Res.string.playlists_header),
                            style = appTypography().headlineLarge
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (loadingStatus != SongsLoadingStatus.DONE) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 4.dp
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.updateCreatePlaylistModal(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Create playlist"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(
                        visible = isCreatePlaylistModalOpen
                    ) {
                        AlertDialogWithTextField(
                            onDismissRequest = { viewModel.updateCreatePlaylistModal(false) },
                            onConfirmation = { inputText ->
                                coroutineScope.launch {
                                    val newPlaylistId = viewModel.storePlaylist(Playlist(name = inputText))
                                    val playlist = Playlist(newPlaylistId, inputText)
                                    onAddPlaylist(playlist)
                                    viewModel.updateCreatePlaylistModal(false)
                                }
                            },
                            dialogTitle = stringResource(Res.string.playlists_create_dialog_title),
                            dialogText = stringResource(Res.string.playlists_create_dialog_desc),
                            icon = Icons.Default.Add
                        )
                    }

                    AnimatedVisibility(
                        visible = isRenameOptionSelected
                    ) {
                        AlertDialogWithTextField(
                            onDismissRequest = { playlistSelectionViewModel.updateRenameOptionSelected(false) },
                            onConfirmation = { inputText ->
                                if (selectedPlaylists.size != 1) return@AlertDialogWithTextField
                                log(
                                    "PlaylistsScreen",
                                    "Renaming playlist ${selectedPlaylists.first().name} to $inputText"
                                )
                                onRenamePlaylist(selectedPlaylists.first().id, inputText)
                                playlistSelectionViewModel.updateRenameOptionSelected(false)
                                playlistSelectionViewModel.updateSelectionMode(false)
                            },
                            dialogTitle = stringResource(Res.string.playlists_rename_dialog_title),
                            dialogText = stringResource(Res.string.playlists_rename_dialog_desc),
                            icon = Icons.Default.Edit
                        )
                    }

                    AnimatedVisibility(
                        visible = isRemovePlaylistModalOpen
                    ) {
                        AlertDialog(
                            onDismissRequest = { viewModel.setIsRemovePlaylistModalOpen(false) },
                            onConfirmation = {
                                playlistSelectionViewModel.updateSelectionMode(false)
                                mainViewModel.removePlaylists(selectedPlaylists)
                                viewModel.setIsRemovePlaylistModalOpen(false)
                            },
                            dialogTitle = stringResource(Res.string.playlists_delete_dialog_title),
                            dialogText = stringResource(Res.string.playlists_delete_dialog_desc),
                            icon = Icons.Default.Delete
                        )
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            playlists == null -> {
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

                            playlists.isEmpty() -> {
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
                                                stringResource(Res.string.playlists_no_playlists),
                                                style = appTypography().bodyMedium
                                            )
                                        }
                                    }
                                }
                            }

                            else -> {
                                for (item in playlists) {
                                    item {
                                        PlaylistItem(
                                            playlist = item,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            onClick = {
                                                if (isSelectionMode) {
                                                    playlistSelectionViewModel.togglePlaylistSelection(item)
                                                } else {
                                                    safeNavigate(navController, PlaylistDetailRoute(item.id))
                                                }
                                            },
                                            onLongClick = {
                                                playlistSelectionViewModel.updateSelectionMode(true)
                                                playlistSelectionViewModel.togglePlaylistSelection(item)
                                            },
                                            isSelectionMode = isSelectionMode,
                                            isSelected = selectedPlaylists.contains(item)
                                        )
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(8.dp + spacerHeight))
                                }
                            }
                        }
                    }
                }
                selectedScreenFeatures?.let { features ->
                    AnimatedVisibility(
                        visible = isSelectionMode && !features.contains(SCREEN_FEATURES.Playlists),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        PlaylistSelectionPill(
                            viewModel = playlistSelectionViewModel,
                            playlistScreenViewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

