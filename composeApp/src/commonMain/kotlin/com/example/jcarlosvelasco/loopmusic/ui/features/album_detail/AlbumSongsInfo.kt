package com.example.jcarlosvelasco.loopmusic.ui.features.album_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.albumsongsinfo_songs
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlbumSongsInfo(
    album: Album,
    albumSongs: List<Song>,
    audioViewModel: AudioViewModel,
    navController: NavHostController,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(Res.string.albumsongsinfo_songs), style = appTypography().headlineMedium)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (song in albumSongs) {
                SongItem(
                    song = song,
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = {
                        audioViewModel.setPlaylistName(album.name)
                        audioViewModel.loadPlaylist(albumSongs, song)
                        audioViewModel.playSong(song)
                        safeNavigate(
                            navController,
                            PlayingRoute,
                        )
                    },
                    onLongClick = {}
                )
            }
        }
    }
}