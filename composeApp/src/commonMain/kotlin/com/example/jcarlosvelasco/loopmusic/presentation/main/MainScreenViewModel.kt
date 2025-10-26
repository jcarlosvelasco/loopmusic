package com.example.jcarlosvelasco.loopmusic.presentation.main

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.presentation.main.manager.ArtworkManagerType
import com.example.jcarlosvelasco.loopmusic.presentation.main.manager.PlaylistManagerType
import com.example.jcarlosvelasco.loopmusic.utils.availableProcessors
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.max


class MainScreenViewModel(
    private val getSelectedMediaFolders: GetSelectedMediaFoldersType,
    private val cacheSongs: CacheSongsType,
    private val getCachedSongs: GetCachedSongsType,
    private val getFileList: GetFileListType,
    private val deleteSongsFromCache: DeleteSongsFromCacheType,
    private val readFileFromPath: ReadFileFromPathType,
    private val coroutineScope: CoroutineScope? = null,
    playlistManagerFactory: (CoroutineScope) -> PlaylistManagerType,
    private val artworkManager: ArtworkManagerType
): ViewModel() {
    private val _customBarHeight = MutableStateFlow(0.dp)
    val customBarHeight = _customBarHeight.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>?>(null)
    var songs = _songs.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>?>(null)
    val albums = _albums.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>?>(null)
    val artists = _artists.asStateFlow()

    private val _loadingStatus = MutableStateFlow<SongsLoadingStatus?>(null)
    var loadingStatus = _loadingStatus.asStateFlow()

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val songsCollection = mutableListOf<Song>()
    private val albumsCollection = mutableMapOf<Long, Album>()
    private val artistsCollection = mutableMapOf<Long, Artist>()

    private val collectionMutex = Mutex()

    private val indexByPath = mutableMapOf<String, Int>()
    private var updateJob: Job? = null
    private val updateDebounceMs = 150L

    private var loadSongsJob: Job? = null

    @OptIn(FlowPreview::class)
    val filteredSongs: StateFlow<List<Song>?> = combine(songs, query) { songList, searchQuery ->
        if (searchQuery.isBlank()) {
            songList
        } else {
            songList?.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.album.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }
        .debounce(150L)
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(FlowPreview::class)
    val filteredAlbums: StateFlow<List<Album>?> = combine(albums, query) { albumList, searchQuery ->
        if (searchQuery.isBlank()) albumList
        else albumList?.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
        .debounce(150L)
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(FlowPreview::class)
    val filteredArtists: StateFlow<List<Artist>?> = combine(artists, query) { artistList, searchQuery ->
        if (searchQuery.isBlank()) artistList
        else artistList?.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
        .debounce(150L)
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())


    private val scope: CoroutineScope
        get() = coroutineScope ?: viewModelScope

    private val playlistManager = playlistManagerFactory(viewModelScope)

    val playlists = playlistManager.playlists

    init {
        log("MainScreenViewModel", "Init")
        loadSongs()
    }

    fun loadSongs() {
        loadSongsJob?.cancel()

        loadSongsJob = scope.launch {
            try {
                log("MainScreenViewModel", "Loading songs with maximum parallelism...")
                _loadingStatus.value = SongsLoadingStatus.LOADING

                val foldersDeferred = async(Dispatchers.IO) { getSelectedMediaFolders.execute() }
                val cachedSongsDeferred = async(Dispatchers.IO) { getCachedSongs.execute() }

                val cachedSongs = cachedSongsDeferred.await()
                log("MainScreenViewModel", "Cached songs loaded: ${cachedSongs.size}")

                if (cachedSongs.isNotEmpty()) {
                    displayCachedSongs(cachedSongs)
                    log("MainScreenViewModel", "Cached songs displayed immediately")
                    //Put songsloadingstatus to cached so user can start searching
                    _loadingStatus.value = SongsLoadingStatus.CACHED
                    artworkManager.loadArtistArtwork(_artists.value ?: emptyList())
                }

                val folders = foldersDeferred.await()
                val fileListDeferred = async(Dispatchers.IO) { getFileList.execute(folders) }

                val filePaths = fileListDeferred.await()
                log("MainScreenViewModel", "File paths loaded: ${filePaths.size}")

                processFileDifferences(filePaths, cachedSongs)

                updateSongsUI()

                artworkManager.loadArtistArtwork(_artists.value ?: emptyList())
                _loadingStatus.value = SongsLoadingStatus.DONE

                // Artwork cleanup
                artworkManager.cleanIfNeeded()
            } catch (e: Exception) {
                log("MainScreenViewModel", "Error loading songs: ${e.message}")
                _loadingStatus.value = SongsLoadingStatus.ERROR
            }
        }
    }

    private suspend fun processFileDifferences(filePaths: List<File>, cachedSongs: List<Song>) {
        val diskPaths = filePaths.map { it.path }.toSet()
        val cachedSongsMap = cachedSongs.associateBy { it.path }
        val cachePaths = cachedSongsMap.keys
        val loadedPaths = _songs.value?.map { it.path }?.toSet() ?: emptySet()

        val pathsToDelete = loadedPaths - diskPaths
        val cachesToDelete = cachePaths - diskPaths
        val filesToProcess = filePaths.filter { file ->
            val cached = cachedSongs.find { it.path == file.path }
            cached == null || file.modificationDate > cached.modificationDate
        }

        log("MainScreenViewModel", "Processing: ${filesToProcess.size} files, deleting: ${pathsToDelete.size} from collection, ${cachesToDelete.size} from cache")

        coroutineScope {
            val delCollection = async { deleteFromCollection(pathsToDelete) }
            val delCache = async { deleteFromCache(cachesToDelete) }
            awaitAll(delCollection, delCache)
        }

        if (filesToProcess.isNotEmpty()) {
            loadOrUpdateSongs(filesToProcess, cachedSongsMap, loadedPaths)
        }
    }

    private suspend fun displayCachedSongs(cachedSongs: List<Song>) {
        collectionMutex.withLock {
            songsCollection.clear()
            albumsCollection.clear()
            artistsCollection.clear()
            indexByPath.clear()

            cachedSongs.sortedWith(songComparator).forEachIndexed { index, song ->
                songsCollection.add(song)
                indexByPath[song.path] = index
                addAlbumToCollection(song.album)
                addArtistToCollection(song.artist)
            }
        }

        updateSongsUI()
        log("MainScreenViewModel", "Cached songs displayed in UI")
    }

    private suspend fun deleteFromCollection(paths: Set<String>) {
        if (paths.isNotEmpty()) {
            log("MainScreenViewModel", "Deleting ${paths.size} songs from loaded songs...")
            playlistManager.removeDeletedSongsFromPlaylists(paths)
            removePathsFromCollection(paths)
        }
    }

    private suspend fun deleteFromCache(paths: Set<String>) {
        if (paths.isNotEmpty()) {
            log("MainScreenViewModel", "Deleting ${paths.size} songs from cache...")
            withContext(Dispatchers.IO) { deleteSongsFromCache.execute(paths.toList()) }
        }
    }

    private suspend fun loadOrUpdateSongs(filePaths: List<File>, cachedSongsMap: Map<String, Song>, loadedPaths: Set<String>) {
        val parallelism = (availableProcessors() - 2).coerceAtLeast(2)
        val dispatcher = Dispatchers.IO.limitedParallelism(parallelism)
        coroutineScope {
            filePaths.map { file ->
                async(dispatcher) {
                    val cachedSong = cachedSongsMap[file.path]
                    if (cachedSong != null) {
                        if (file.modificationDate > cachedSong.modificationDate) {
                            //log("MainScreenViewModel","Updating song from cache: ${file.path}")
                            val newSong = readFileFromPath.execute(file)
                            cacheSongs(listOf(newSong))
                            addOrUpdateSongInCollection(newSong)
                        } else if (file.path !in loadedPaths) {
                            addOrUpdateSongInCollection(cachedSong)
                        }
                    } else {
                        //log("MainScreenViewModel", "Loading song from disk: ${file.path}")
                        val newSong = readFileFromPath.execute(file)
                        cacheSongs(listOf(newSong))
                        addOrUpdateSongInCollection(newSong)
                    }
                }
            }.awaitAll()
        }
    }

    private fun scheduleUIUpdate() {
        updateJob?.cancel()
        updateJob = scope.launch {
            delay(updateDebounceMs)
            updateSongsUI()
        }
    }

    private fun cacheSongs(list: List<Song>) {
        scope.launch {
            val artworkList = list.map { it.album.artwork }
            val songsWithoutArtwork = list.map { it.copy(album = it.album.copy(artwork = null)) }
            withContext(Dispatchers.IO) {
                cacheSongs.execute(songsWithoutArtwork, artworkList)
            }
        }
    }

    private suspend fun addOrUpdateSongInCollection(song: Song) {
        var changed = false
        collectionMutex.withLock {
            val existingIndex = indexByPath[song.path]
                ?: songsCollection.indexOfFirst { it.path == song.path }.also { idx ->
                    if (idx >= 0) indexByPath[song.path] = idx
                }

            if (existingIndex >= 0) {
                val old = songsCollection[existingIndex]
                if (old != song) {
                    removeAlbumIfLastSong(old.album.id, song.path)
                    removeArtistIfLastSong(old.artist.id, song.path)

                    songsCollection[existingIndex] = song
                    songsCollection.removeAt(existingIndex)
                    indexByPath.remove(song.path)

                    val insertAt = findInsertionIndex(song)
                    log("MainScreenViewModel", "Updating song '${song.name}' - moving from index $existingIndex to $insertAt")
                    songsCollection.add(insertAt, song)
                    rebuildLocalIndexes(minOf(insertAt, existingIndex))
                    changed = true
                }
            } else {
                val insertAt = findInsertionIndex(song)
                log("MainScreenViewModel", "Adding new song '${song.name}' at index $insertAt of ${songsCollection.size}")

                songsCollection.add(insertAt, song)
                rebuildLocalIndexes(insertAt)

                addAlbumToCollection(song.album)
                addArtistToCollection(song.artist)
                changed = true
            }
        }
        if (changed) {
            log("MainScreenViewModel", "Song collection changed, updating UI. Total songs: ${songsCollection.size}")
            scheduleUIUpdate()
        }
    }

    private suspend fun removePathsFromCollection(paths: Collection<String>) {
        if (paths.isEmpty()) return
        var changed = false
        collectionMutex.withLock {
            val toRemove = paths.mapNotNull { p ->
                val idx = indexByPath[p]
                    ?: songsCollection.indexOfFirst { it.path == p }.also { found ->
                        if (found >= 0) indexByPath[p] = found
                    }
                if (idx >= 0) idx else null
            }.distinct().sortedDescending()

            if (toRemove.isNotEmpty()) {
                var minTouched = Int.MAX_VALUE
                val removedAlbumIds = mutableSetOf<Long>()
                val removedArtistIds = mutableSetOf<Long>()

                for (idx in toRemove) {
                    val song = songsCollection[idx]
                    removedAlbumIds.add(song.album.id)
                    removedArtistIds.add(song.artist.id)
                    songsCollection.removeAt(idx)
                    minTouched = minOf(minTouched, idx)
                }

                paths.forEach { indexByPath.remove(it) }
                rebuildLocalIndexes(if (minTouched == Int.MAX_VALUE) 0 else minTouched)

                removedAlbumIds.forEach { albumId ->
                    if (songsCollection.none { it.album.id == albumId }) {
                        albumsCollection.remove(albumId)
                    }
                }

                removedArtistIds.forEach { artistId ->
                    if (songsCollection.none { it.artist.id == artistId }) {
                        artistsCollection.remove(artistId)
                    }
                }

                changed = true
            }
        }
        if (changed) scheduleUIUpdate()
    }

    private fun addAlbumToCollection(album: Album) {
        if (!albumsCollection.containsKey(album.id)) {
            albumsCollection[album.id] = album
        }
    }

    private fun addArtistToCollection(artist: Artist) {
        if (!artistsCollection.containsKey(artist.id)) {
            artistsCollection[artist.id] = artist
        }
    }

    private fun removeAlbumIfLastSong(albumId: Long, excludePath: String) {
        val hasOtherSongs = songsCollection.any {
            it.album.id == albumId && it.path != excludePath
        }
        if (!hasOtherSongs) {
            albumsCollection.remove(albumId)
        }
    }

    private fun removeArtistIfLastSong(artistID: Long, excludePath: String) {
        val hasOtherSongs = songsCollection.any {
            it.artist.id == artistID && it.path != excludePath
        }
        if (!hasOtherSongs) {
            artistsCollection.remove(artistID)
        }
    }

    private fun findInsertionIndex(song: Song): Int {
        if (songsCollection.isEmpty()) return 0
        val idx = songsCollection.binarySearch(song, comparator = songComparator)
        return if (idx >= 0) {
            idx + 1
        } else {
            -(idx + 1)
        }
    }

    private fun rebuildLocalIndexes(from: Int) {
        val start = max(0, from)
        for (i in start until songsCollection.size) {
            indexByPath[songsCollection[i].path] = i
        }
    }

    private suspend fun updateSongsUI() {
        collectionMutex.withLock {
            _songs.value = songsCollection.toList()

            _albums.value = albumsCollection.values.sortedWith(albumComparator)

            _artists.value = artistsCollection.values.sortedWith(artistComparator)
        }
    }

    fun setCustomBarHeight(height: Dp) {
        _customBarHeight.value = height
    }

    fun updateQuery(value: String) {
        _query.value = value
    }

    fun addSongsToPlaylists(songs: Set<Song>, playlists: Set<Playlist>) { playlistManager.addSongsToPlaylists(songs, playlists) }
    fun removePlaylists(playlists: Set<Playlist>) { playlistManager.removePlaylists(playlists) }

    fun renamePlaylist(playlistId: Long, newName: String) { playlistManager.renamePlaylist(playlistId, newName) }
    fun addPlaylistToCollection(playlist: Playlist) { playlistManager.addPlaylistToCollection(playlist) }
    fun removeSongsFromPlaylist(songs: Set<Song>, playlist: Playlist) { playlistManager.removeSongsFromPlaylist(songs, playlist) }
}