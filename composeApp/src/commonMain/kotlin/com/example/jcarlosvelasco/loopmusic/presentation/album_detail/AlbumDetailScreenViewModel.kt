package com.example.jcarlosvelasco.loopmusic.presentation.album_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetFullQualityArtworkType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailScreenViewModel(
    private val getFullQualityArtwork: GetFullQualityArtworkType
): ViewModel() {
    private val _album = MutableStateFlow<Album?>(null)
    val album = _album.asStateFlow()

    private val _fullQualityArtwork = MutableStateFlow<ByteArray?>(null)
    val fullQualityArtwork = _fullQualityArtwork.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    var albumID: Long? = null

    init {
        log("AlbumDetailViewModel", "Init")
    }

    fun loadAlbumFromMemory(albumId: Long, allSongs: List<Song>, allAlbums: List<Album>) {
        viewModelScope.launch {
            try {
                val foundAlbum = allAlbums.find { it.id == albumId }
                val foundSongs = allSongs.filter { it.album.id == albumId }
                    .sortedBy { it.trackNumber ?: Int.MAX_VALUE }

                _album.value = foundAlbum
                _songs.value = foundSongs

                if (_songs.value.isNotEmpty()) {
                    getFullQualityArtwork(_songs.value[0].path)
                }

                log("AlbumDetailViewModel","Loaded album from memory: ${foundAlbum?.name}")
            } catch (e: Exception) {
                log("AlbumDetailViewModel","Error loading from memory: $e")
            }
        }
    }

    fun getFullQualityArtwork(songPath: String): ByteArray? {
        viewModelScope.launch {
            log("AlbumDetailViewModel", "Loading full quality artwork for: $songPath")
            _fullQualityArtwork.value = null
            val value = withContext(Dispatchers.IO) {
                getFullQualityArtwork.execute(songPath)
            }
            _fullQualityArtwork.value = value
        }
        return null
    }

    fun updateAlbumID(value: Long) {
        albumID = value
    }
}