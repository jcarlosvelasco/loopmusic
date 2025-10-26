package com.example.jcarlosvelasco.loopmusic.ui.features.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.main.SongsLoadingStatus
import com.example.jcarlosvelasco.loopmusic.presentation.search.SearchScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.features.albums.AlbumItem
import com.example.jcarlosvelasco.loopmusic.ui.features.artists.ArtistItem
import com.example.jcarlosvelasco.loopmusic.ui.features.songs.SongItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.AlbumDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.ArtistDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    loadingStatus: SongsLoadingStatus?,
    fromOthers: Boolean = false,
    filteredSongs: List<Song>?,
    filteredAlbums: List<Album>?,
    filteredArtists: List<Artist>?,
    query: String,
    onUpdateQuery: (String) -> Unit,
    viewModel: SearchScreenViewModel = koinViewModel(),
    audioViewModel: AudioViewModel
) {
    val isTextFieldFocused by viewModel.isTextFieldFocused.collectAsStateWithLifecycle()
    val isSongsButtonActive by viewModel.isSongsButtonActive.collectAsStateWithLifecycle()
    val isAlbumsButtonActive by viewModel.isAlbumsButtonActive.collectAsStateWithLifecycle()
    val isArtistsButtonActive by viewModel.isArtistsButtonActive.collectAsStateWithLifecycle()

    val playlistTitle = stringResource(Res.string.playlist_search)

    Scaffold {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            if (fromOthers) {
                IconButton(
                    onClick = { safePopBackStack(navController) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
                Spacer(modifier = Modifier.padding(12.dp))
            }

            AnimatedVisibility(
                visible = !isTextFieldFocused,
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(Res.string.search_header),
                            style = appTypography().headlineLarge
                        )
                        if (loadingStatus == SongsLoadingStatus.LOADING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            viewModel.updateTextFieldFocus(focusState.isFocused)
                        },
                    searchBarText = query,
                    setSearchBarText = { onUpdateQuery(it) },
                    setSearchBarActive = { viewModel.updateTextFieldFocus(it) },
                    searchBarActive = isTextFieldFocused,
                    onClearClick = { onUpdateQuery("") }
                )
            }

            Spacer(modifier = Modifier.padding(12.dp))

            AnimatedVisibility(
                visible = isTextFieldFocused,
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isSongsButtonActive) {
                        item {
                            Button(
                                onClick = { viewModel.updateSongsButtonActive(false) }
                            ) {
                                Text(stringResource(Res.string.search_songs_btn), style = appTypography().bodyLarge)
                            }
                        }
                    } else {
                        item {
                            OutlinedButton(
                                onClick = { viewModel.updateSongsButtonActive(true) }
                            ) {
                                Text(stringResource(Res.string.search_songs_btn), style = appTypography().bodyLarge)
                            }
                        }
                    }

                    if (isAlbumsButtonActive) {
                        item {
                            Button(
                                onClick = { viewModel.updateAlbumsButtonActive(false) }
                            ) {
                                Text(stringResource(Res.string.search_albums_btn), style = appTypography().bodyLarge)
                            }
                        }
                    } else {
                        item {
                            OutlinedButton(
                                onClick = { viewModel.updateAlbumsButtonActive(true) }
                            ) {
                                Text(stringResource(Res.string.search_albums_btn), style = appTypography().bodyLarge)
                            }
                        }
                    }

                    if (isArtistsButtonActive) {
                        item {
                            Button(
                                onClick = { viewModel.updateArtistsButtonActive(false) }
                            ) {
                                Text(stringResource(Res.string.search_artists_btn), style = appTypography().bodyLarge)
                            }
                        }
                    } else {
                        item {
                            OutlinedButton(
                                onClick = { viewModel.updateArtistsButtonActive(true) }
                            ) {
                                Text(stringResource(Res.string.search_artists_btn), style = appTypography().bodyLarge)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(12.dp))

            if (query == "") {
                if (loadingStatus == SongsLoadingStatus.CACHED || loadingStatus == SongsLoadingStatus.DONE) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                        Text(stringResource(Res.string.search_text), style = appTypography().headlineMedium)
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            else {
                if (loadingStatus == SongsLoadingStatus.CACHED || loadingStatus == SongsLoadingStatus.DONE) {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        filteredSongs?.let {
                            if (it.isNotEmpty() && isSongsButtonActive) {
                                item {
                                    Text(stringResource(Res.string.search_songs_text), style = appTypography().bodyLarge)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                                it.forEach { song ->
                                    item {
                                        SongItem(
                                            song = song,
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            onClick = {
                                                audioViewModel.setPlaylistName(playlistTitle)
                                                audioViewModel.loadPlaylist(listOf(song), song)
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

                        filteredAlbums?.let {
                            if (it.isNotEmpty() && isAlbumsButtonActive) {
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(stringResource(Res.string.search_albums_text), style = appTypography().bodyLarge)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                                it.forEach { album ->
                                    item {
                                        AlbumItem(
                                            item = album,
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            onClick = {
                                                safeNavigate(navController, AlbumDetailRoute(album.id))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        filteredArtists?.let {
                            if (it.isNotEmpty() && isArtistsButtonActive) {
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(stringResource(Res.string.search_artists_text), style = appTypography().bodyLarge)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                                it.forEach { artist ->
                                    item {
                                        ArtistItem(
                                            item = artist,
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            onClick = {
                                                safeNavigate(navController, ArtistDetailRoute(artist.id))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if (filteredSongs.isNullOrEmpty() && filteredAlbums.isNullOrEmpty() && filteredArtists.isNullOrEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text("No results found", style = appTypography().bodyLarge)
                                }
                            }
                        }
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}