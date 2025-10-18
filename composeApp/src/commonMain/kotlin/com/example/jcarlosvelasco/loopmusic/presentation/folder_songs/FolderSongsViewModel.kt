package com.example.jcarlosvelasco.loopmusic.presentation.folder_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetFolderFromFolderPathType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FolderSongsViewModel(
    private val getFolderFromFolderPath: GetFolderFromFolderPathType
): ViewModel() {
    private val _folder = MutableStateFlow<Folder?>(null)
    val folder = _folder.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()

    var folderPath: String? = null

    fun loadFolderAndSongsFromFolderPath(allSongs: List<Song>) {
        viewModelScope.launch {
            folderPath?.let { path ->
                withContext(Dispatchers.IO) {
                    val folder = getFolderFromFolderPath.execute(path)
                    _folder.value = folder

                    val filteredSongs = allSongs.filter { it.path.contains(folderPath ?: "") }
                    _songs.value = filteredSongs
                }
            }
        }
    }

    fun updateFolderPath(value: String, allSongs: List<Song>) {
        folderPath = value
        loadFolderAndSongsFromFolderPath(allSongs)
    }
}