package com.example.jcarlosvelasco.loopmusic.ui.features.home

import ShimmerTextSkeleton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.home.HomeScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.SongsLoadingStatus
import com.example.jcarlosvelasco.loopmusic.ui.navigation.SettingsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.skeleton.SongItemBigSkeleton
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.home_com_up_next
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun HomeScreen(
    navController: NavHostController,
    loadingStatus: SongsLoadingStatus?,
    viewModel: HomeScreenViewModel = koinViewModel(),
    audioViewModel: AudioViewModel = koinViewModel(),
    spacerHeight: Dp
) {
    val playlist by audioViewModel.playlist.collectAsStateWithLifecycle()
    val playlistLoadingState by audioViewModel.playListLoadingState.collectAsStateWithLifecycle()
    val currentIndex by audioViewModel.currentPlayingSongIndexInPlaylist.collectAsStateWithLifecycle()
    val listMode by audioViewModel.listMode.collectAsStateWithLifecycle()

    val formattedPlaylist by remember(playlist, currentIndex, listMode) {
        derivedStateOf {
            currentIndex?.let { index ->
                listMode?.let { mode ->
                    viewModel.formatUpcomingPlaylist(
                        playlist,
                        index,
                        10,
                        mode
                    )
                }
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp)
                    ) {
                        Text(
                            "Loop",
                            style = appTypography().headlineLarge,
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
                                onClick = { safeNavigate(navController, SettingsRoute) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Open settings"
                                )
                            }
                        }
                    }
                }

                if (!playlistLoadingState) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ShimmerTextSkeleton(textStyle = appTypography().headlineMedium)
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                            ) {
                                repeat(10) {
                                    item {
                                        SongItemBigSkeleton()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    formattedPlaylist?.let {
                        if (it.isNotEmpty()) {
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        stringResource(Res.string.home_com_up_next),
                                        style = appTypography().headlineMedium,
                                    )
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    ) {
                                        for (item in it) {
                                            item {
                                                SongItemBig(song = item, modifier = Modifier, onClick = {})
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    //TODO: STILL SOME SPACE CROPPED
                    Spacer(modifier = Modifier.height(spacerHeight))
                }

                /*
        Text(
            text = "Recently played",
            style = appTypography().headlineMedium,
        )
        
        Text(
            text = "Last added",
            style = appTypography().headlineMedium,
        )*/
            }
        }
    }
}