package com.example.jcarlosvelasco.loopmusic.ui.features.playlist_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.ui.components.PlaylistArtwork
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaylistInfo(
    playlist: Playlist,
    songs: List<Song>,
    isLandscape: Boolean,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    artworkList: List<ByteArray>,
) {
    Column(
        modifier = if (isLandscape) Modifier else Modifier.fillMaxWidth(),
        horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally,
    ) {
        PlaylistArtwork(
            artworkList = artworkList
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            playlist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().headlineMedium
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            "${songs.count()} ${
                if (songs.count() == 1) stringResource(Res.string.album_detail_song) else stringResource(Res.string.album_detail_songs)
            }",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = appTypography().bodyMedium,
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        if (songs.isNotEmpty()) {
            if (isLandscape) {
                Column(
                    modifier = Modifier.width(250.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onPlayClick,
                    ) {
                        Text(
                            stringResource(Res.string.playlist_info_play),
                            style = appTypography().bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Button(
                        onClick = onShuffleClick,
                    ) {
                        Text(
                            stringResource(Res.string.playlist_info_shuffle),
                            style = appTypography().bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = onPlayClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            stringResource(Res.string.playlist_info_play),
                            style = appTypography().bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Button(
                        onClick = onShuffleClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            stringResource(Res.string.playlist_info_shuffle),
                            style = appTypography().bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}