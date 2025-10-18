package com.example.jcarlosvelasco.loopmusic.presentation.playlist_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetPlaylistFromPlaylistIdType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val getPlaylistFromPlaylistId: GetPlaylistFromPlaylistIdType
): ViewModel() {
    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist = _playlist.asStateFlow()

    private val _playlistArtwork = MutableStateFlow<List<ByteArray>>(emptyList())
    val playlistArtwork = _playlistArtwork.asStateFlow()

    var playlistId: Long? = null

    fun setPlaylistId(id: Long) {
        playlistId = id
        loadPlaylist(id)
    }

    private fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistWithPaths = getPlaylistFromPlaylistId.execute(playlistId)
            log("PlaylistDetailViewModel", "Loaded playlist: $playlistWithPaths")
            playlistWithPaths?.let { playlist ->
                _playlist.value = playlist
            }
        }
    }

    fun removeSongsFromPlaylist(songs: Set<Song>) {
        val songPathsToRemove = songs.map { it.path }.toSet()
        _playlist.value?.let { playlist ->
            val updatedSongPaths = playlist.songPaths.filterNot { it in songPathsToRemove }.toMutableList()
            _playlist.value = playlist.copy(songPaths = updatedSongPaths)
        }
        log("PlaylistDetailViewModel", "playlist after songs removal: ${_playlist.value}")
    }

    fun loadPlaylistArtwork(playlistSongs: List<Song>) {
        val uniqueAlbums = playlistSongs
            .mapNotNull { it.album.artwork }
            .distinctBy { it.contentHashCode() }

        _playlistArtwork.value = when {
            uniqueAlbums.size >= 4 -> uniqueAlbums.take(4)
            uniqueAlbums.isNotEmpty() -> listOf(uniqueAlbums.first())
            else -> emptyList()
        }
    }
}