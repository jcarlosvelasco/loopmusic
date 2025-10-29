package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Platform
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.platform.PlatformObject
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState

@Composable
fun ScreenWithPlayingPill(
    navController: NavHostController,
    selectedScreenFeatures: Set<SCREEN_FEATURES>?,
    playingScreenViewModel: PlayingScreenViewModel,
    modifier: Modifier = Modifier,
    selectedFeature: SCREEN_FEATURES,
    condition: Boolean = true,
    currentPlayingSong: Song?,
    mediaState: MediaState,
    onPlayPauseClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val shouldModifyPadding = selectedScreenFeatures?.contains(selectedFeature) == false && PlatformObject.platform == Platform.IOS

    Box(modifier = if (shouldModifyPadding) modifier.then(modifier.padding(horizontal = 16.dp)) else modifier) {
        content()

        ConditionalPlayingPill(
            navController = navController,
            selectedScreenFeatures = selectedScreenFeatures,
            playingScreenViewModel = playingScreenViewModel,
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedFeature = selectedFeature,
            condition = condition,
            currentPlayingSong = currentPlayingSong,
            mediaState = mediaState,
            onPlayPauseClick = onPlayPauseClick
        )
    }
}