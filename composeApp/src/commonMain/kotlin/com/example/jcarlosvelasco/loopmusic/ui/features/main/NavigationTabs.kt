package com.example.jcarlosvelasco.loopmusic.ui.features.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class NavigationTab(
    val titleRes: StringResource,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(Res.string.home, Icons.Filled.Home, Icons.Outlined.Home),
    SONGS(Res.string.songs, Icons.Filled.MusicNote, Icons.Outlined.MusicNote),
    OTHER(Res.string.other, Icons.Filled.Menu, Icons.Outlined.Menu),
    ALBUMS(Res.string.albums, Icons.Filled.Album, Icons.Outlined.Album),
    SEARCH(Res.string.search, Icons.Filled.Search, Icons.Outlined.Search),
    ARTISTS(Res.string.artists, Icons.Filled.Person, Icons.Outlined.Person),
    FOLDERS(Res.string.folders, Icons.Filled.Folder, Icons.Outlined.Folder),
    PLAYLISTS(Res.string.playlists, Icons.AutoMirrored.Filled.PlaylistPlay, Icons.AutoMirrored.Outlined.PlaylistPlay)
}