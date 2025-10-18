package com.example.jcarlosvelasco.loopmusic.ui.features.playing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode
import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode
import com.example.jcarlosvelasco.loopmusic.presentation.playing.PlayingScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import com.example.jcarlosvelasco.loopmusic.ui.theme.urbanistFontFamily
import com.example.jcarlosvelasco.loopmusic.utils.needsContrast
import com.kmpalette.DominantColorState

@Composable
fun PlayingInfo(
    song: Song,
    currentPosition: Float,
    audioViewModel: AudioViewModel,
    dominantColorState: DominantColorState<ByteArray>,
    viewModel: PlayingScreenViewModel,
    listMode: ListMode?,
    mediaState: MediaState,
    playMode: PlayMode?,
    themeViewModel: ThemeViewModel
) {
    val theme by themeViewModel.theme.collectAsStateWithLifecycle()
    val scale = animateFloatAsState(if (mediaState == MediaState.PAUSED) 1.05f else 1.0f, label = "")

    val isDarkMode = when (theme) {
        Theme.DARK -> true
        Theme.LIGHT -> false
        Theme.SYSTEM -> isSystemInDarkTheme()
        null -> isSystemInDarkTheme()
    }

    val themeColor = dominantColorState.color
    val onThemeColor = dominantColorState.onColor

    val safeThemeColor = if (needsContrast(themeColor, isDarkMode)) {
        MaterialTheme.colorScheme.primary
    } else themeColor

    val safeOnThemeColor = if (needsContrast(themeColor, isDarkMode)) {
        MaterialTheme.colorScheme.onPrimary
    } else onThemeColor

    Column {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                text = song.name,
                fontSize = 20.sp,
                fontFamily = urbanistFontFamily(),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                text = "${song.artist.name} · ${song.album.name}",
                style = appTypography().bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Slider(
                value = currentPosition,
                onValueChange = { newValue ->
                    audioViewModel.seekTo(newValue)
                },
                valueRange = 0f..song.duration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = safeThemeColor,
                    activeTrackColor = safeThemeColor,
                    inactiveTrackColor = safeThemeColor.copy(alpha = 0.3f)
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    viewModel.formatTime(currentPosition),
                    fontSize = 14.sp,
                    fontFamily = urbanistFontFamily()
                )
                Text(
                    viewModel.formatTime(song.duration.toFloat()),
                    fontSize = 14.sp,
                    fontFamily = urbanistFontFamily()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    audioViewModel.onListModeChanged()
                }
            ) {
                when (listMode) {
                    ListMode.ONE_SONG -> {
                        Icon(
                            imageVector = Icons.Default.LooksOne,
                            contentDescription = "One Song"
                        )
                    }

                    ListMode.ONE_TIME -> {
                        Icon(
                            imageVector = Icons.Default.RepeatOne,
                            contentDescription = "One Time"
                        )
                    }

                    ListMode.LOOP -> {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "All Songs"
                        )
                    }

                    null -> Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "All Songs"
                    )
                }
            }

            IconButton(
                onClick = {
                    audioViewModel.playPreviousSong()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous"
                )
            }

            Button(
                onClick = {
                    audioViewModel.onPlayPauseClick()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .scale(scale.value),
                colors = ButtonDefaults.buttonColors(
                    containerColor = safeThemeColor,
                    contentColor = safeOnThemeColor
                )
            ) {
                if (mediaState == MediaState.PLAYING) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause"
                    )
                } else if (mediaState == MediaState.PAUSED) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }

            IconButton(
                onClick = {
                    audioViewModel.playNextSong()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next"
                )
            }

            IconButton(
                onClick = {
                    audioViewModel.onPlayModeChanged()
                }
            ) {
                when (playMode) {
                    PlayMode.SHUFFLE -> {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle"
                        )
                    }

                    PlayMode.ORDERED -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Ordered"
                        )
                    }

                    null -> Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Ordered"
                    )
                }
            }
        }
    }
}