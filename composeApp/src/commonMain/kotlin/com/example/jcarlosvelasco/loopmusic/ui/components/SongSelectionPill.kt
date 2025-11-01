package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.song_selection_addtoplaylist
import loopmusic.composeapp.generated.resources.song_selection_removefromplaylist
import loopmusic.composeapp.generated.resources.song_selection_select
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SongSelectionPill(
    modifier: Modifier = Modifier,
    songsViewModel: SongsViewModel = koinViewModel(),
    allowRemoveFromPlaylist: Boolean = false,
    onRemoveFromPlaylist: (Set<Song>, Playlist) -> Unit = { _, _ -> },
    fromPlaylist: Playlist? = null,
) {
    val selectedSongs by songsViewModel.selectedSongs.collectAsStateWithLifecycle()
    val count = selectedSongs.size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .then(modifier)
        ,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(stringResource(Res.string.song_selection_select), style = appTypography().headlineMedium)
                    Text("$count", style = appTypography().headlineMedium)
                }

                IconButton(onClick = {
                    songsViewModel.updateSelectionMode(false)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    onClick = {
                        songsViewModel.setIsPlaylistSelectionMode(true)
                    },
                    enabled = count > 0
                ) {
                    Text(stringResource(Res.string.song_selection_addtoplaylist), style = appTypography().bodyLarge)
                }

                if (allowRemoveFromPlaylist) {
                    Button(
                        onClick = {
                            fromPlaylist?.let {
                                onRemoveFromPlaylist(selectedSongs, fromPlaylist)
                                songsViewModel.updateSelectionMode(false)
                            }
                        },
                        enabled = count > 0
                    ) {
                        Text(stringResource(Res.string.song_selection_removefromplaylist), style = appTypography().bodyLarge)
                    }
                }
            }
        }
    }
}