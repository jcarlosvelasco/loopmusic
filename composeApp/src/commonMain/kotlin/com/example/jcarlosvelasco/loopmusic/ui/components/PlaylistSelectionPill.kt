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
import com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection.PlaylistSelectionViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.playlists.PlaylistsViewModel
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.playlist_select_delete
import loopmusic.composeapp.generated.resources.playlist_selection_header
import loopmusic.composeapp.generated.resources.playlist_selection_rename
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaylistSelectionPill(
    modifier: Modifier = Modifier,
    viewModel: PlaylistSelectionViewModel,
    playlistScreenViewModel: PlaylistsViewModel
) {
    val selectedPlaylists by viewModel.selectedPlaylists.collectAsStateWithLifecycle()
    val count = selectedPlaylists.size

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
                    Text(stringResource(Res.string.playlist_selection_header), style = appTypography().headlineMedium)
                    Text("$count", style = appTypography().headlineMedium)
                }

                IconButton(onClick = {
                    viewModel.updateSelectionMode(false)
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
                        selectedPlaylists.let {
                            playlistScreenViewModel.setIsRemovePlaylistModalOpen(true)
                        }
                    },
                    enabled = selectedPlaylists.isNotEmpty()
                ) {
                    Text(stringResource(Res.string.playlist_select_delete), style = appTypography().bodyLarge)
                }

                Button(
                    onClick = {
                        selectedPlaylists.let {
                            viewModel.updateRenameOptionSelected(true)
                        }
                    },
                    enabled = selectedPlaylists.size == 1
                ) {
                    Text(stringResource(Res.string.playlist_selection_rename), style = appTypography().bodyLarge)
                }
            }
        }
    }
}