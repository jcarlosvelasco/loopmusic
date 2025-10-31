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
            log("PlaylistManager", "Loaded playlists: $result")
            _playlists.value = result
        }
    }

    override fun removePlaylists(playlists: Set<Playlist>) {
        val originalPlaylists = _playlists.value?.map { it.copy() }

        _playlists.value = _playlists.value?.toMutableList()?.apply {
            removeAll(playlists)
        }

        scope.launch(Dispatchers.IO) {
            try {
                playlists.forEach {
                    deletePlaylist.execute(it.id)
                }
            } catch (e: Exception) {
                log("PlaylistManager", "Error deleting playlists: ${e.message}")

                withContext(Dispatchers.Main) {
                    _playlists.value = originalPlaylists
                }
            }
        }
    }

    override fun renamePlaylist(playlistId: Long, newName: String) {
        val originalPlaylists = _playlists.value?.map { it.copy() }

        _playlists.value = _playlists.value?.map { playlist ->
            if (playlist.id == playlistId) {
                playlist.copy(name = newName)
            } else {
                playlist
            }
        }

        scope.launch(Dispatchers.IO) {
            try {
                val playlistToRename = _playlists.value?.find { it.id == playlistId }
                log("PlaylistManager", "Renaming playlist '${playlistToRename?.name}' to '$newName'")
                playlistToRename?.let {
                    renamePlaylist.execute(playlistId, newName)
                }
            } catch (e: Exception) {
                log("PlaylistManager", "Error renaming playlist: ${e.message}")

                withContext(Dispatchers.Main) {
                    _playlists.value = originalPlaylists
                }
            }
        }
    }

    override fun removeSongsFromPlaylist(songs: Set<Song>, playlist: Playlist) {
        log("PlaylistManager", "Removing ${songs.size} songs from playlist '${playlist.name}'")
        val songPathsToRemove = songs.map { it.path }.toSet()

        val originalPlaylists = _playlists.value?.map { it.copy(songPaths = it.songPaths.toMutableList()) }

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
            try {
                songs.forEach { song ->
                    removeSongFromPlaylist.execute(song.path, playlist.id)
                }
            } catch (e: Exception) {
                log("PlaylistManager", "Error removing songs: ${e.message}")

                withContext(Dispatchers.Main) {
                    _playlists.value = originalPlaylists
                }
            }
        }
    }

    override fun removeDeletedSongsFromPlaylists(deletedPaths: Set<String>) {
        val originalPlaylists = _playlists.value?.map { it.copy(songPaths = it.songPaths.toMutableList()) }

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
            try {
                deletedPaths.forEach { path ->
                    _playlists.value?.forEach { playlist ->
                        if (!playlist.songPaths.contains(path)) {
                            removeSongFromPlaylist.execute(path, playlist.id)
                        }
                    }
                }
            } catch (e: Exception) {
                log("MainScreenViewModel", "Error removing deleted songs: ${e.message}")

                withContext(Dispatchers.Main) {
                    _playlists.value = originalPlaylists
                }
            }
        }
    }

    override fun addPlaylistToCollection(playlist: Playlist) {
        _playlists.value = _playlists.value?.toMutableList()?.apply {
            add(playlist)
        }
        log("PlaylistManager", "Added playlist '${playlist.name}' to collection, id: ${playlist.id}")
    }

    override fun addSongsToPlaylists(songs: Set<Song>, playlists: Set<Playlist>) {
        val originalPlaylists = _playlists.value?.map { it.copy(songPaths = it.songPaths.toMutableList()) }

        val songPaths = songs.map { it.path }.toSet()

        _playlists.value = _playlists.value?.map { playlist ->
            if (playlist in playlists) {
                val newPaths = songPaths.filterNot { it in playlist.songPaths }
                if (newPaths.isNotEmpty()) {
                    log("PlaylistManager", "Adding ${newPaths.size} new songs to playlist '${playlist.name} ${playlist.id}'")
                    playlist.copy(
                        songPaths = (playlist.songPaths + newPaths).toMutableList()
                    )
                } else {
                    log("PlaylistManager", "All songs already exist in playlist '${playlist.name}'")
                    playlist
                }
            } else {
                playlist
            }
        }

        scope.launch(Dispatchers.IO) {
            try {
                playlists.forEach { playlist ->
                    val oldPaths = originalPlaylists?.find { it.id == playlist.id }?.songPaths ?: emptyList()
                    val newSongs = songs.filter { it.path !in oldPaths }

                    log("PlaylistManager", "newSongs: ${newSongs.size}")
                    if (newSongs.isNotEmpty()) {
                        scope.launch(Dispatchers.IO) {
                            addSongsToPlaylist.execute(newSongs.toSet(), playlist.id)
                        }
                    }
                }
            } catch (e: Exception) {
                log("PlaylistManager", "Error adding songs to playlists: ${e.message}")
                withContext(Dispatchers.Main) {
                    _playlists.value = originalPlaylists
                }
            }
        }
    }
}