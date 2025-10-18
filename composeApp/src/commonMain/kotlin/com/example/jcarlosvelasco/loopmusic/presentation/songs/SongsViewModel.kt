package com.example.jcarlosvelasco.loopmusic.presentation.songs

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongsViewModel: ViewModel() {
    private val _selectedSongs = MutableStateFlow<Set<Song>>(emptySet())
    val selectedSongs = _selectedSongs.asStateFlow()

    private val _selectedPlaylists = MutableStateFlow<Set<Playlist>>(emptySet())
    val selectedPlaylists = _selectedPlaylists.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode.asStateFlow()

    private val _isPlaylistSelectionMode = MutableStateFlow(false)
    val isPlaylistSelectionMode = _isPlaylistSelectionMode.asStateFlow()

    init {
        log("SongsViewModel", "Init")
    }

    fun removeSongFromSelected(song: Song) {
        _selectedSongs.value -= song
    }

    fun addSongToSelected(song: Song) {
        _selectedSongs.value += song
    }

    fun isSongSelected(song: Song): Boolean {
        return _selectedSongs.value.contains(song)
    }

    fun updateSelectionMode(value: Boolean) {
        _isSelectionMode.value = value

        if (!value) {
            _selectedSongs.value = emptySet()
        }
    }

    fun addPlaylistToSelected(playlist: Playlist) {
        _selectedPlaylists.value += playlist
    }

    fun removePlaylistFromSelected(playlist: Playlist) {
        _selectedPlaylists.value -= playlist
    }

    fun isPlaylistSelected(playlist: Playlist): Boolean {
        return _selectedPlaylists.value.contains(playlist)
    }

    fun setIsPlaylistSelectionMode(value: Boolean) {
        _isPlaylistSelectionMode.value = value

        if (!value) {
            _selectedPlaylists.value = emptySet()
        }
    }
}