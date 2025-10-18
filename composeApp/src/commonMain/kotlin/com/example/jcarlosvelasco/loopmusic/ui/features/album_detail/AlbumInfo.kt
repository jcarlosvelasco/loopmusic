package com.example.jcarlosvelasco.loopmusic.ui.features.album_detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.album_detail_song
import loopmusic.composeapp.generated.resources.album_detail_songs
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlbumInfo(
    album: Album,
    albumSongs: List<Song>,
    isLandscape: Boolean,
    fullQualityArtwork: ByteArray?,
) {
    val highResAlpha by animateFloatAsState(
        targetValue = if (fullQualityArtwork != null) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Column(
        modifier = if (isLandscape) Modifier else Modifier.fillMaxWidth(),
        horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = album.artwork,
                contentDescription = "Low-res artwork",
                modifier = Modifier.matchParentSize()
            )

            if (fullQualityArtwork != null) {
                AsyncImage(
                    model = fullQualityArtwork,
                    contentDescription = "High-res artwork",
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { alpha = highResAlpha }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            album.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().headlineMedium,
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            "${album.artist.name} · ${albumSongs.count()} ${if (albumSongs.count() == 1) stringResource(Res.string.album_detail_song) else stringResource(Res.string.album_detail_songs)}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().bodyMedium,
        )
    }
}