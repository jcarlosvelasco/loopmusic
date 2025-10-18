package com.example.jcarlosvelasco.loopmusic.domain.model

import com.example.jcarlosvelasco.loopmusic.ui.features.main.NavigationTab
import com.example.jcarlosvelasco.loopmusic.ui.navigation.AlbumsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.ArtistsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.FoldersRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.HomeRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.NavigationRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlaylistsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.SearchRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.SongsRoute
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.feature_albums
import loopmusic.composeapp.generated.resources.feature_artists
import loopmusic.composeapp.generated.resources.feature_folders
import loopmusic.composeapp.generated.resources.feature_home
import loopmusic.composeapp.generated.resources.feature_playlists
import loopmusic.composeapp.generated.resources.feature_search
import loopmusic.composeapp.generated.resources.feature_songs
import org.jetbrains.compose.resources.StringResource

sealed class SCREEN_FEATURES(val titleRes: StringResource) {

    data object Songs : SCREEN_FEATURES(Res.string.feature_songs) {
        override fun toStorageString() = "songs"
    }

    data object Home : SCREEN_FEATURES(Res.string.feature_home) {
        override fun toStorageString() = "home"
    }

    data object Albums : SCREEN_FEATURES(Res.string.feature_albums) {
        override fun toStorageString() = "albums"
    }

    data object Search : SCREEN_FEATURES(Res.string.feature_search) {
        override fun toStorageString() = "search"
    }

    data object Artists : SCREEN_FEATURES(Res.string.feature_artists) {
        override fun toStorageString() = "artists"
    }

    data object Folders : SCREEN_FEATURES(Res.string.feature_folders) {
        override fun toStorageString() = "folders"
    }

    data object Playlists: SCREEN_FEATURES(Res.string.feature_playlists) {
        override fun toStorageString() = "playlists"
    }

    abstract fun toStorageString(): String

    companion object {
        fun fromStorageString(str: String): SCREEN_FEATURES? = when(str) {
            "songs" -> Songs
            "home" -> Home
            "albums" -> Albums
            "search" -> Search
            "artists" -> Artists
            "folders" -> Folders
            "playlists" -> Playlists
            else -> null
        }

        fun all(): List<SCREEN_FEATURES> = listOf(
            Songs, Home, Albums, Search, Artists, Folders, Playlists
        )

        fun SCREEN_FEATURES.toNavigationTab(): NavigationTab = when(this) {
            Home -> NavigationTab.HOME
            Songs -> NavigationTab.SONGS
            Albums -> NavigationTab.ALBUMS
            Search -> NavigationTab.SEARCH
            Artists -> NavigationTab.ARTISTS
            Folders -> NavigationTab.FOLDERS
            Playlists -> NavigationTab.PLAYLISTS
        }

        fun SCREEN_FEATURES.getRoute(fromOthers: Boolean = false): NavigationRoute = when(this) {
            Albums -> AlbumsRoute(fromOthers)
            Songs -> SongsRoute(fromOthers)
            Search -> SearchRoute(fromOthers)
            Artists -> ArtistsRoute(fromOthers)
            Folders -> FoldersRoute(fromOthers)
            Home -> HomeRoute
            Playlists -> PlaylistsRoute(fromOthers)
        }
    }
}