package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun PlaylistArtwork(
    artworkList: List<ByteArray>,
    modifier: Modifier = Modifier,
    size: Dp = 250.dp,
    cornerRadius: Dp = 8.dp
) {
    when {
        artworkList.isEmpty() -> return
        artworkList.size == 1 -> SingleArtwork(
            artwork = artworkList[0],
            size = size,
            cornerRadius = cornerRadius,
            modifier = modifier
        )
        else -> GridArtwork(
            artworkList = artworkList.take(4),
            size = size,
            cornerRadius = cornerRadius,
            modifier = modifier
        )
    }
}

@Composable
private fun SingleArtwork(
    artwork: ByteArray,
    size: Dp,
    cornerRadius: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        ArtworkImage(
            artwork = artwork,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun GridArtwork(
    artworkList: List<ByteArray>,
    size: Dp,
    cornerRadius: Dp,
    modifier: Modifier = Modifier
) {
    val itemSize = (size.value / 2).dp - 1.dp

    Column(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius)),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(2) { index ->
                if (index < artworkList.size) {
                    ArtworkImage(
                        artwork = artworkList[index],
                        modifier = Modifier.size(itemSize)
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(2) { index ->
                val artworkIndex = index + 2
                if (artworkIndex < artworkList.size) {
                    ArtworkImage(
                        artwork = artworkList[artworkIndex],
                        modifier = Modifier.size(itemSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtworkImage(
    artwork: ByteArray,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(artwork)
            .crossfade(true)
            .build(),
        placeholder = ColorPainter(Color.Gray),
        error = ColorPainter(Color.Gray),
        contentDescription = "Artwork",
        modifier = modifier
    )
}