package com.example.jcarlosvelasco.loopmusic.ui.features.folders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.folders.FoldersScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.SongsLoadingStatus
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.components.ScreenWithPlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.features.folder_songs.FolderItem
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.navigation.FolderSongsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.folders_header
import loopmusic.composeapp.generated.resources.folders_no_folders
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoldersScreen(
    navController: NavHostController,
    listState: LazyListState,
    loadingStatus: SongsLoadingStatus?,
    spacerHeight: Dp,
    fromOthers: Boolean = false,
    viewModel: FoldersScreenViewModel = koinViewModel(),
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit
) {
    val folders by viewModel.folders.collectAsStateWithLifecycle()

    Scaffold {
        ScreenWithPlayingPill(
            navController = navController,
            selectedScreenFeatures = selectedScreenFeatures,
            playingScreenViewModel = playingScreenViewModel,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(top = 16.dp),
            selectedFeature = SCREEN_FEATURES.Folders,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = onPlayPauseClick
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (fromOthers) {
                    IconButton(
                        onClick = { safePopBackStack(navController) }
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
                        stringResource(Res.string.folders_header),
                        style = appTypography().headlineLarge
                    )
                    if (loadingStatus != SongsLoadingStatus.DONE) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 4.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(12.dp))

                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when {
                        folders == null -> {
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

                        folders!!.isEmpty() -> {
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
                                            stringResource(Res.string.folders_no_folders),
                                            style = appTypography().bodyMedium
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            for (folder in folders) {
                                item {
                                    FolderItem(
                                        onClick = { safeNavigate(navController, FolderSongsRoute(folder.path)) },
                                        folder = folder
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
        }
    }
}