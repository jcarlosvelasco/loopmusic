package com.example.jcarlosvelasco.loopmusic.presentation.artist_detail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistDetailScreenViewModel(): ViewModel() {

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist = _artist.asStateFlow()

    private val _artistAlbums = MutableStateFlow<List<Album>?>(null)
    val artistAlbums = _artistAlbums.asStateFlow()

    private val _artistSongs = MutableStateFlow<List<Song>?>(null)
    val artistSongs = _artistSongs.asStateFlow()

    private val _featuredSongs = MutableStateFlow<List<Song>?>(null)
    val featuredSongs = _featuredSongs.asStateFlow()

    private val _dominantColor = MutableStateFlow<Color?>(null)
    val dominantColor: StateFlow<Color?> = _dominantColor.asStateFlow()

    private val _dominantOnColor = MutableStateFlow<Color?>(null)
    val dominantOnColor: StateFlow<Color?> = _dominantOnColor.asStateFlow()

    var artistID: Long? = null

    init {
        log("ArtistDetailViewModel", "Init")
    }

    fun updateDominantColors(color: Color, onColor: Color) {
        _dominantColor.value = color
        _dominantOnColor.value = onColor
    }

    fun loadAlbumFromMemory(artistID: Long, allArtists: List<Artist>, allSongs: List<Song>) {
        viewModelScope.launch {
            try {
                _artist.value = allArtists.filter { it.id == artistID }[0]
                _artistSongs.value = allSongs.filter { it.artist.id == artistID }.sortedWith(compareBy({ it.album.year }, { it.album.name }))
                _artistAlbums.value = _artistSongs.value?.map { it.album }?.distinctBy { it.id }?.sortedBy { it.year }

                val allArtistSongs = allSongs.filter { it.artist.name.contains(_artist.value!!.name) }

                val allArtistsSongsMinusArtistSongs = allArtistSongs
                    .subtract(allSongs
                        .filter { it.artist.name == _artist.value!!.name }
                        .toSet()
                    )

                _featuredSongs.value = allArtistsSongsMinusArtistSongs.toList().sortedBy { it.album.year }
            } catch (e: Exception) {
                log("ArtistDetailViewModel","Error loading from memory: $e")
            }
        }
    }

    fun updateArtistID(value: Long) {
        artistID = value
    }
}