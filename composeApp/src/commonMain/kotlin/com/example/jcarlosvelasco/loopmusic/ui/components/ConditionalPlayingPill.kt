package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.main.PlayingPill
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate

@Composable
fun ConditionalPlayingPill(
    navController: NavHostController,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    modifier: Modifier = Modifier,
    selectedFeature: SCREEN_FEATURES,
    condition: Boolean = true,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit,
) {
    val density = LocalDensity.current

    selectedScreenFeatures?.let { features ->
        if (!features.contains(selectedFeature) && condition) {
            AnimatedVisibility(
                visible = currentPlayingSong != null,
                modifier = modifier
            ) {
                currentPlayingSong?.let { song ->
                    PlayingPill(
                        mediaState = mediaState,
                        song = song,
                        onClick = { safeNavigate(navController, PlayingRoute) },
                        onPlayPauseClick = onPlayPauseClick,
                        modifier = Modifier.onGloballyPositioned {
                            playingScreenViewModel.setPlayingPillHeight(
                                with(density) {
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