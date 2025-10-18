package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.songs.SongsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.addtoplaylist_add
import loopmusic.composeapp.generated.resources.addtoplaylist_back
import loopmusic.composeapp.generated.resources.addtoplaylist_header
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddToPlaylistPill(
    modifier: Modifier = Modifier,
    songsViewModel: SongsViewModel = koinViewModel(),
    mainScreenViewModel: MainScreenViewModel = koinViewModel()
) {
    val selectedSongs by songsViewModel.selectedSongs.collectAsStateWithLifecycle()
    val selectedPlaylists by songsViewModel.selectedPlaylists.collectAsStateWithLifecycle()
    val playlists by mainScreenViewModel.playlists.collectAsStateWithLifecycle()

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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(Res.string.addtoplaylist_header), style = appTypography().headlineMedium)

            playlists?.let {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 150.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (playlist in it) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        if (songsViewModel.isPlaylistSelected(playlist)) {
                                            songsViewModel.removePlaylistFromSelected(playlist)
                                        }
                                        else {
                                            songsViewModel.addPlaylistToSelected(playlist)
                                        }
                                    }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(playlist.name, style = appTypography().bodyMedium)

                                Checkbox(
                                    checked = selectedPlaylists.contains(playlist),
                                    onCheckedChange = null
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(onClick = {
                    songsViewModel.setIsPlaylistSelectionMode(false)
                }) {
                    Text(stringResource(Res.string.addtoplaylist_back), style = appTypography().bodyLarge)
                }
                Button(onClick = {
                    mainScreenViewModel.addSongsToPlaylists(selectedSongs, selectedPlaylists)
                    songsViewModel.setIsPlaylistSelectionMode(false)
                    songsViewModel.updateSelectionMode(false)
                }) {
                    Text(stringResource(Res.string.addtoplaylist_add), style = appTypography().bodyLarge)
                }
            }
        }
    }
}