package com.example.jcarlosvelasco.loopmusic.ui.features.artist_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistInfo(
    artist: Artist,
    artistSongs: List<Song>,
    artistAlbums: List<Album>,
    isLandscape: Boolean,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit
) {
    Column(
        modifier = if (isLandscape) Modifier else Modifier.fillMaxWidth(),
        horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally,
    ) {
        artist.artwork?.let {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    placeholder = ColorPainter(Color.Gray),
                    error = ColorPainter(Color.Gray),
                    contentDescription = "Artist artwork",
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            artist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().headlineMedium,
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            "${artistSongs.count()} ${if (artistSongs.count() == 1) stringResource(Res.string.album_detail_song) else stringResource(Res.string.album_detail_songs)} · ${artistAlbums.count()} ${if (artistAlbums.count() == 1) stringResource(Res.string.artist_detail_album) else stringResource(Res.string.artist_detail_albums2)}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().bodyMedium,
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        if (isLandscape) {
            Column(
                modifier = Modifier.width(250.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPlayClick,
                ) {
                    Text(
                        stringResource(Res.string.artist_info_play),
                        style = appTypography().bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Button(
                    onClick = onShuffleClick,
                ) {
                    Text(
                        stringResource(Res.string.artist_info_shuffle),
                        style = appTypography().bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        stringResource(Res.string.artist_info_play),
                        style = appTypography().bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Button(
                    onClick = onShuffleClick,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        stringResource(Res.string.artist_info_shuffle),
                        style = appTypography().bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}