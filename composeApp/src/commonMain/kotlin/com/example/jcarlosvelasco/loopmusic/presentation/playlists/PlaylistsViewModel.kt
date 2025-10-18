package com.example.jcarlosvelasco.loopmusic.presentation.playlists

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.usecase.StorePlaylistType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class PlaylistsViewModel(
    private val storePlaylist: StorePlaylistType
): ViewModel() {
    private val _isCreatePlaylistModalOpen = MutableStateFlow(false)
    val isCreatePlaylistModalOpen = _isCreatePlaylistModalOpen.asStateFlow()

    private val _isRemovePlaylistModalOpen = MutableStateFlow(false)
    val isRemovePlaylistModalOpen = _isRemovePlaylistModalOpen.asStateFlow()

    fun updateCreatePlaylistModal(value: Boolean) {
        _isCreatePlaylistModalOpen.value = value
    }

    suspend fun storePlaylist(playlist: Playlist): Long {
        log("PlaylistsViewModel", "Storing playlist: $playlist")
        return withContext(Dispatchers.IO) {
            log("PlaylistsViewModel", "Storing playlist in background: $playlist")
            storePlaylist.execute(playlist)
        }
    }

    fun setIsRemovePlaylistModalOpen(value: Boolean) {
        _isRemovePlaylistModalOpen.value = value
    }
}