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
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.artist_detail_featured
import org.jetbrains.compose.resources.stringResource

@Composable
fun FeaturedArtistSongs(
    featuredSongs: List<Song>,
    artist: Artist,
    navController: NavHostController,
    viewModel: SongsViewModel,
    onPlaySong: (String, List<Song>, Song) -> Unit
) {
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedSongs by viewModel.selectedSongs.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(Res.string.artist_detail_featured), style = appTypography().headlineMedium)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            featuredSongs.forEach {
                SongItem(
                    song = it,
                    isSelected = selectedSongs.contains(it),
                    modifier = Modifier.padding(vertical = 8.dp),
                    onClick = {
                        if (isSelectionMode) {
                            if (viewModel.isSongSelected(it)) {
                                viewModel.removeSongFromSelected(it)
                            } else {
                                viewModel.addSongToSelected(it)
                            }
                        } else {
                            onPlaySong(artist.name, featuredSongs, it)
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
                            viewModel.addSongToSelected(it)
                        } else {
                            if (viewModel.isSongSelected(it)) {
                                viewModel.removeSongFromSelected(it)
                            } else {
                                viewModel.addSongToSelected(it)
                            }
                        }
                    }
                )
            }
        }
    }
}