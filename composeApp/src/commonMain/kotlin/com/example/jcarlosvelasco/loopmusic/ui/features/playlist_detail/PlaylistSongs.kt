package com.example.jcarlosvelasco.loopmusic.ui.features.playlist_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.albumsongsinfo_songs
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaylistSongs(
    playlistSongs: List<Song>,
    selectedSongs: Set<Song>,
    isSelectionMode: Boolean,
    songsViewModel: SongsViewModel,
    playlist: Playlist? = null,
    audioViewModel: AudioViewModel,
    navController: NavHostController,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (playlistSongs.isNotEmpty()) {
            Text(stringResource(Res.string.albumsongsinfo_songs), style = appTypography().headlineMedium)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (item in playlistSongs) {
                    SongItem(
                        item,
                        Modifier.padding(bottom = 16.dp),
                        isSelected = selectedSongs.contains(item),
                        onClick = {
                            if (isSelectionMode) {
                                if (songsViewModel.isSongSelected(item)) {
                                    songsViewModel.removeSongFromSelected(item)
                                } else {
                                    songsViewModel.addSongToSelected(item)
                                }
                            } else {
                                playlist?.let {
                                    audioViewModel.setPlaylistName(it.name)
                                    audioViewModel.loadPlaylist(playlistSongs, item)
                                    audioViewModel.playSong(item)
                                    safeNavigate(
                                        navController,
                                        PlayingRoute,
                                    )
                                }
                            }
                        },
                        isSelectionMode = isSelectionMode,
                        onLongClick = {
                            if (!isSelectionMode) {
                                songsViewModel.updateSelectionMode(true)
                                songsViewModel.addSongToSelected(item)
                            } else {
                                if (songsViewModel.isSongSelected(item)) {
                                    songsViewModel.removeSongFromSelected(item)
                                } else {
                                    songsViewModel.addSongToSelected(item)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}