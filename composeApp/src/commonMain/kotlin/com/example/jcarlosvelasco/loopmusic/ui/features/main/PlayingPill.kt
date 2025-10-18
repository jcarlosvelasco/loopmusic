package com.example.jcarlosvelasco.loopmusic.ui.features.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography

@Composable
fun PlayingPill(
    mediaState: MediaState,
    onClick: () -> Unit,
    song: Song,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .then(modifier)
        ,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(song.album.artwork)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Artwork",
                    placeholder = ColorPainter(Color.Gray),
                    error = ColorPainter(Color.Gray),
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = song.name,
                        style = appTypography().bodyLarge
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = "${song.artist.name} - ${song.album.name}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = appTypography().bodySmall
                    )
                }
            }

            IconButton(
                onClick = onPlayPauseClick
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
        }
    }
}