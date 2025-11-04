package com.example.jcarlosvelasco.loopmusic.ui.features.albums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.main.SongsLoadingStatus
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.components.ConditionalPlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.navigation.AlbumDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.albums_header
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    navController: NavHostController,
    loadingStatus: SongsLoadingStatus?,
    albums: List<Album>?,
    listState: LazyListState,
    spacerHeight: Dp,
    fromOthers: Boolean = false,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            stringResource(Res.string.albums_header),
                            style = appTypography().headlineLarge
                        )
                        if (loadingStatus != SongsLoadingStatus.DONE) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (fromOthers) {
                        IconButton(onClick = { safePopBackStack(navController) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
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
                .padding(top = 4.dp)
                .fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                when {
                    albums == null -> {
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

                    albums.isEmpty() && loadingStatus == SongsLoadingStatus.LOADING -> {
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

                    albums.isEmpty() && loadingStatus == SongsLoadingStatus.DONE -> {
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
                                        "No albums added",
                                        style = appTypography().bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        for (album in albums) {
                            item {
                                AlbumItem(
                                    album,
                                    onClick = {
                                        safeNavigate(navController, AlbumDetailRoute(album.id))
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

            ConditionalPlayingPill(
                navController = navController,
                selectedScreenFeatures = selectedScreenFeatures,
                playingScreenViewModel = playingScreenViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                selectedFeature = SCREEN_FEATURES.Albums,
                currentPlayingSong = currentPlayingSong,
                mediaState = mediaState,
                onPlayPauseClick = onPlayPauseClick,
            )
        }
    }
}