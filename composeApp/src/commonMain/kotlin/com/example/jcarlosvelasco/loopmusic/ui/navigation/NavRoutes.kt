package com.example.jcarlosvelasco.loopmusic.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute

@Serializable
data object HomeRoute: NavigationRoute

@Serializable
data class SongsRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class AlbumsRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class ArtistsRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class SearchRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class FoldersRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class PlaylistsRoute(
    val fromOthers: Boolean = false
): NavigationRoute

@Serializable
data class FolderSongsRoute(val folderPath: String) : NavigationRoute

@Serializable
data class MediaFoldersRoute(
    val fromSettings: Boolean = false
) : NavigationRoute

@Serializable
data object MainRoute: NavigationRoute

@Serializable
data object SettingsRoute: NavigationRoute

@Serializable
data object PlayingRoute: NavigationRoute

@Serializable
data object SettingsTabsRoute: NavigationRoute

@Serializable
data class AlbumDetailRoute(val albumId: Long) : NavigationRoute

@Serializable
data class PlaylistDetailRoute(val playlistId: Long) : NavigationRoute

@Serializable
data class ArtistDetailRoute(val artistId: Long) : NavigationRoute
