package com.example.jcarlosvelasco.loopmusic.presentation.playlist_selection

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistSelectionViewModel: ViewModel() {
    private val _selectedPlaylists = MutableStateFlow<Set<Playlist>>(emptySet())
    val selectedPlaylists = _selectedPlaylists.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode.asStateFlow()

    private val _isRenameOptionSelected = MutableStateFlow(false)
    val isRenameOptionSelected = _isRenameOptionSelected.asStateFlow()

    fun updateRenameOptionSelected(value: Boolean) {
        _isRenameOptionSelected.value = value
    }

    fun togglePlaylistSelection(playlist: Playlist) {
        val selectedPlaylists = _selectedPlaylists.value
        if (selectedPlaylists.contains(playlist)) {
            _selectedPlaylists.value = selectedPlaylists.minus(playlist)
        } else {
            _selectedPlaylists.value = selectedPlaylists.plus(playlist)
        }
    }

    fun updateSelectionMode(value: Boolean) {
        _isSelectionMode.value = value

        if (!value) {
            _selectedPlaylists.value = emptySet()
        }
    }
}