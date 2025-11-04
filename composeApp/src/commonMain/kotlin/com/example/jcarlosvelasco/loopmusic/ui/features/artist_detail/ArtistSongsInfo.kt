package com.example.jcarlosvelasco.loopmusic.ui.features.artist_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.artist_detail_songs
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistSongsInfo(
    artistName: String,
    albumSongs: List<Song>,
    navController: NavHostController,
    viewModel: SongsViewModel,
    onPlaySong: (String, List<Song>, Song) -> Unit
) {
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedSongs by viewModel.selectedSongs.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(Res.string.artist_detail_songs), style = appTypography().headlineMedium)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (song in albumSongs) {
                SongItem(
                    song = song,
                    modifier = Modifier.padding(vertical = 8.dp),
                    isSelected = selectedSongs.contains(song),
                    onClick = {
                        if (isSelectionMode) {
                            if (viewModel.isSongSelected(song)) {
                                viewModel.removeSongFromSelected(song)
                            } else {
                                viewModel.addSongToSelected(song)
                            }
                        } else {
                            onPlaySong(artistName, albumSongs, song)
                            safeNavigate(
                                navController,
                                PlayingRoute,
                            )
                        }
                    },
                    isSelectionMode = isSelectionMode,
                    onLongClick = {
                        if (!isSelectionMode) {
                            viewModel.updateSelectionMode(true)
                            viewModel.addSongToSelected(song)
                        } else {
                            if (viewModel.isSongSelected(song)) {
                                viewModel.removeSongFromSelected(song)
                            } else {
                                viewModel.addSongToSelected(song)
                            }
                        }
                    }
                )
            }
        }
    }
}