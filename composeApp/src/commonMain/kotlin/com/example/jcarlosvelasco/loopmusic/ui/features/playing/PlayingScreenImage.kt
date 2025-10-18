package com.example.jcarlosvelasco.loopmusic.ui.features.playing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.jcarlosvelasco.loopmusic.domain.model.Song

@Composable
fun PlayingScreenImage(
    currentPlayingSong: Song,
    fullQualityArtwork: ByteArray?,
) {
    val highResAlpha by animateFloatAsState(
        targetValue = if (fullQualityArtwork != null) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = currentPlayingSong.album.artwork,
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
}