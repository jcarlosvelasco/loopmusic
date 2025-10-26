package com.example.jcarlosvelasco.loopmusic.presentation.main.manager

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface PlaylistManagerType {
    fun loadPlaylists()
    fun removePlaylists(playlists: Set<Playlist>)
    fun renamePlaylist(playlistId: Long, newName: String)
    fun removeSongsFromPlaylist(songs: Set<Song>, playlist: Playlist)
    fun removeDeletedSongsFromPlaylists(deletedPaths: Set<String>)
    fun addPlaylistToCollection(playlist: Playlist)
    fun addSongsToPlaylists(songs: Set<Song>, playlists: Set<Playlist>)

    val playlists: StateFlow<List<Playlist>?>
}

class PlaylistManager(
    private val getPlaylists: GetPlaylistsType,
    private val deletePlaylist: DeletePlaylistType,
    private val renamePlaylist: RenamePlaylistType,
    private val removeSongFromPlaylist: RemoveSongFromPlaylistType,
    private val addSongsToPlaylist: AddSongsToPlaylistType,
    private val scope: CoroutineScope
): PlaylistManagerType {
    private val _playlists = MutableStateFlow<List<Playlist>?>(null)
    override val playlists = _playlists.asStateFlow()

    init {
        log("PlaylistManager", "Init")
        loadPlaylists()
    }

    override fun loadPlaylists() {
        scope.launch(Dispatchers.IO) {
            val result = getPlaylists.execute()
            _playlists.value = result
        }
    }

    override fun removePlaylists(playlists: Set<Playlist>) {
        _playlists.value = _playlists.value?.toMutableList()?.apply {
            removeAll(playlists)
        }

        scope.launch(Dispatchers.IO) {
            playlists.forEach {
                deletePlaylist.execute(it.id)
            }
        }
    }

    override fun renamePlaylist(playlistId: Long, newName: String) {
        _playlists.value = _playlists.value?.map { playlist ->
            if (playlist.id == playlistId) {
                playlist.copy(name = newName)
            } else {
                playlist
            }
        }

        log("MainScreenViewModel", "Rename 1")
        scope.launch(Dispatchers.IO) {
            val playlistToRename = _playlists.value?.find { it.id == playlistId }
            log("MainScreenViewModel", "Renaming playlist '${playlistToRename?.name}' to '$newName'")
            playlistToRename?.let {
                renamePlaylist.execute(playlistId, newName)
            }
        }
    }

    override fun removeSongsFromPlaylist(songs: Set<Song>, playlist: Playlist) {
        log("MainScreenViewModel", "Removing ${songs.size} songs from playlist '${playlist.name}'")
        val songPathsToRemove = songs.map { it.path }.toSet()

        _playlists.value = _playlists.value?.map { pl ->
            if (pl.id == playlist.id) {
                pl.copy(
                    songPaths = pl.songPaths.filterNot { it in songPathsToRemove }.toMutableList()
                )
            } else {
                pl
            }
        }

        scope.launch(Dispatchers.IO) {
            songs.forEach { song ->
                removeSongFromPlaylist.execute(song.path, playlist.id)
            }
        }
    }

    override fun removeDeletedSongsFromPlaylists(deletedPaths: Set<String>) {
        _playlists.value = _playlists.value?.map { playlist ->
            val originalSize = playlist.songPaths.size
            val updatedPaths = playlist.songPaths.filterNot { it in deletedPaths }

            if (updatedPaths.size != originalSize) {
                log(
                    "MainScreenViewModel",
                    "Removed ${originalSize - updatedPaths.size} deleted songs from playlist '${playlist.name}'"
                )
                playlist.copy(songPaths = updatedPaths.toMutableList())
            } else {
                playlist
            }
        }

        scope.launch(Dispatchers.IO) {
            deletedPaths.forEach { path ->
                _playlists.value?.forEach { playlist ->
                    if (!playlist.songPaths.contains(path)) {
                        removeSongFromPlaylist.execute(path, playlist.id)
                    }
                }
            }
        }
    }

    override fun addPlaylistToCollection(playlist: Playlist) {
        _playlists.value = _playlists.value?.toMutableList()?.apply {
            add(playlist)
        }
    }

    override fun addSongsToPlaylists(songs: Set<Song>, playlists: Set<Playlist>) {
        scope.launch {
            val songPaths = songs.map { it.path }.toSet()

            _playlists.value = _playlists.value?.map { playlist ->
                if (playlist in playlists) {
                    val newPaths = songPaths.filterNot { it in playlist.songPaths }
                    if (newPaths.isNotEmpty()) {
                        log("MainScreenViewModel", "Adding ${newPaths.size} new songs to playlist '${playlist.name}'")
                        playlist.copy(
                            songPaths = (playlist.songPaths + newPaths).toMutableList()
                        )
                    } else {
                        log("MainScreenViewModel", "All songs already exist in playlist '${playlist.name}'")
                        playlist
                    }
                } else {
                    playlist
                }
            }

            withContext(Dispatchers.IO) {
                playlists.forEach { playlist ->
                    val existingPaths = _playlists.value?.find { it.id == playlist.id }?.songPaths ?: emptyList()
                    val newSongs = songs.filter { it.path !in existingPaths }

                    if (newSongs.isNotEmpty()) {
                        addSongsToPlaylist.execute(newSongs.toSet(), playlist.id)
                    }
                }
            }
        }
    }
}