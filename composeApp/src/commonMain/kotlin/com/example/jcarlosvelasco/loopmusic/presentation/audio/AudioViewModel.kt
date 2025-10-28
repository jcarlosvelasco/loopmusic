package com.example.jcarlosvelasco.loopmusic.presentation.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.PublishNowPlayingType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.SetPlayingType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.ios_specific.UpdateElapsedType
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.utils.isExternalPath
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update

class AudioViewModel(
    private val playSong: PlaySongType,
    private val pauseSong: PauseSongType,
    private val resumeSong: ResumeSongType,
    private val seekTo: SeekToType,
    private val getListMode: GetListModeType,
    private val getPlayMode: GetPlayModeType,
    private val setListMode: SetListModeType,
    private val setPlayMode: SetPlayModeType,
    private val getCurrentMediaPlayerPosition: GetCurrentMediaPlayerPositionType,
    private val getCurrentPlayerState: GetCurrentPlayerStateType,
    private val publishNowPlaying: PublishNowPlayingType,
    private val updateElapsed: UpdateElapsedType,
    private val setPlaying: SetPlayingType,
    private val getPlaybackState: GetPlaybackStateType,
    private val savePlaybackState: SavePlaybackStateType,
    private val putSongInPlayerType: PutSongInPlayerType,
    private val getAlbumArtwork: GetAlbumArtworkType,
    setOnSongEndCallback: SetOnSongEndCallbackType
): ViewModel() {
    private val _playListLoadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val playListLoadingState = _playListLoadingState.asStateFlow()

    private val _currentPlayingSong: MutableStateFlow<Song?> = MutableStateFlow(null)
    val currentPlayingSong = _currentPlayingSong.asStateFlow()

    private val _previousCurrentPlayingSongAlbum: MutableStateFlow<Album?> = MutableStateFlow(null)
    val previousCurrentPlayingSongAlbum = _previousCurrentPlayingSongAlbum.asStateFlow()

    private val _mediaState: MutableStateFlow<MediaState> = MutableStateFlow(MediaState.NONE)
    val mediaState = _mediaState.asStateFlow()

    private val _playlist: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    val playlist = _playlist.asStateFlow()

    private val _currentPlayingSongIndexInPlaylist: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentPlayingSongIndexInPlaylist = _currentPlayingSongIndexInPlaylist.asStateFlow()

    private val _currentPosition: MutableStateFlow<Float> = MutableStateFlow(0f)
    val currentPosition = _currentPosition.asStateFlow()

    private val _listMode: MutableStateFlow<ListMode?> = MutableStateFlow(null)
    val listMode = _listMode.asStateFlow()

    private val _playMode: MutableStateFlow<PlayMode?> = MutableStateFlow(null)
    val playMode = _playMode.asStateFlow()

    private val _playlistName: MutableStateFlow<String> = MutableStateFlow("")
    val playlistName = _playlistName.asStateFlow()

    private var progressJob: Job? = null
    private val progressIntervalMs = 250L

    init {
        log("AudioViewModel", "Init")
        loadPlaybackState()
        loadListMode()
        loadPlayMode()
        observeStateChanges()

        setOnSongEndCallback.execute {
            when (_listMode.value) {
                ListMode.ONE_TIME if _currentPlayingSongIndexInPlaylist.value == _playlist.value.size - 1 -> {
                    log("AudioViewModel", "Progress updates: ONE_TIME")
                    pauseSong()
                    seekTo(0F)
                }

                ListMode.ONE_SONG -> {
                    log("AudioViewModel", "Progress updates: ONE_SONG")
                    pauseSong()
                    seekTo(0F)
                }

                else -> {
                    log("AudioViewModel", "Progress updates: LOOP")
                    playNextSong()
                }
            }
        }
    }

    fun onPlayPauseClick() {
        when(mediaState.value) {
            MediaState.PLAYING -> pauseSong()
            MediaState.PAUSED -> resumeSong()
            MediaState.NONE -> {}
        }
    }

    fun pauseSong() {
        log("AudioViewModel", "Pause")
        _mediaState.value = MediaState.PAUSED
        pauseSong.execute()
        setPlaying.execute(false)
        stopProgressUpdates()
    }

    fun resumeSong() {
        log("AudioViewModel", "Resume")
        _mediaState.value = MediaState.PLAYING
        resumeSong.execute()
        setPlaying.execute(true)
        startProgressUpdates()
    }

    fun playSong(song: Song) {
        log("AudioViewModel", "Play song: ${song.name}")
        _previousCurrentPlayingSongAlbum.value = _currentPlayingSong.value?.album
        _currentPlayingSong.value = song
        _mediaState.value = MediaState.PLAYING
        _currentPosition.value = 0f
        playSong.execute(song)
        setPlaying.execute(true)
        publishNowPlaying.execute(song, 0L, true)
        savePlaybackState()
        startProgressUpdates()
    }

    fun seekTo(value: Float) {
        _currentPosition.value = value
        seekTo.execute(value)
    }

    fun playPreviousSong() {
        _currentPlayingSongIndexInPlaylist.value?.let {
            if (it > 0) {
                playSong(_playlist.value[it - 1])
                _currentPlayingSongIndexInPlaylist.value = it - 1
            }
            else {
                playSong(_playlist.value[_playlist.value.size - 1])
                _currentPlayingSongIndexInPlaylist.value = _playlist.value.size - 1
            }
            savePlaybackState()
        }
    }

    fun playNextSong() {
        log("AudioViewModel", "Play next song")
        _currentPlayingSongIndexInPlaylist.value?.let {
            if (it < _playlist.value.size - 1) {
                playSong(_playlist.value[it + 1])
                _currentPlayingSongIndexInPlaylist.value = it + 1
            }
            else {
                playSong(_playlist.value[0])
                _currentPlayingSongIndexInPlaylist.value = 0
            }
            savePlaybackState()
        }
    }

    fun loadPlaylist(playlist: List<Song>, song: Song) {
        _playListLoadingState.value = false
        when (_playMode.value) {
            PlayMode.ORDERED -> {
                _playlist.value = playlist
            }
            PlayMode.SHUFFLE -> {
                _playlist.value = playlist.shuffled()
            }
            else -> {}
        }

        _currentPlayingSongIndexInPlaylist.value = _playlist.value.indexOf(song)
        _playListLoadingState.value = true
        savePlaybackState()
    }

    fun loadPlaylistAndPlay(playlist: List<Song>, isShuffled: Boolean) {
        _playListLoadingState.value = false
        _playMode.value = if (isShuffled) PlayMode.SHUFFLE else PlayMode.ORDERED
        _playlist.value = if (isShuffled) playlist.shuffled() else playlist
        _currentPlayingSongIndexInPlaylist.value = 0
        _playListLoadingState.value = true
        playSong(_playlist.value[0])
    }

    private fun startProgressUpdates() {
        log("AudioViewModel", "Start progress updates")
        stopProgressUpdates()

        progressJob = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            try {
                while (isActive) {
                    //log("AudioViewModel", "Progress updates")

                    val duration = currentPlayingSong.value?.duration?.toFloat() ?: 0f
                    if (duration <= 0f) {
                        val realState = getCurrentPlayerState.execute()
                        if (realState != mediaState.value) {
                            _mediaState.value = realState
                        }
                        delay(progressIntervalMs)
                        continue
                    }

                    //log("AudioViewModel", "Progress updates: ${duration - currentPosition.value}")

                    val realState = getCurrentPlayerState.execute()
                    if (realState != mediaState.value) {
                        _mediaState.value = realState
                    }

                    //log("AudioViewModel", "real state: ${realState.toString()}")

                    val position = getCurrentMediaPlayerPosition.execute()
                        .coerceIn(0f, duration)

                    //log("AudioViewModel", "position: $position")

                    _currentPosition.value = position
                    updateElapsed.execute(position.toLong(), true)

                    delay(progressIntervalMs)
                }
            } catch (e: CancellationException) {
                log("AudioViewModel", "Progress updates cancelled: ${e.message}")
                throw e
            } catch (e: Exception) {
                log("AudioViewModel", "Error in progress updates: ${e.message}")
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun loadListMode() {
        val value = getListMode.execute()
        log("AudioViewModel", "Loaded list mode: $value")
        if (value == null) {
            val newMode = ListMode.LOOP
            _listMode.value = newMode
            setListMode.execute(newMode)
        }
        else {
            _listMode.value = value
        }
    }

    private fun loadPlayMode() {
        val value = getPlayMode.execute()
        log("AudioViewModel", "Loaded play mode: $value")
        if (value == null) {
            val newMode = PlayMode.ORDERED

            _playMode.value = newMode

            setPlayMode.execute(newMode)
        }
        else {
            _playMode.value = value
        }
    }

    fun onListModeChanged() {
        val newMode = when(_listMode.value) {
            ListMode.LOOP -> ListMode.ONE_TIME
            ListMode.ONE_TIME -> ListMode.ONE_SONG
            ListMode.ONE_SONG -> ListMode.LOOP
            else -> ListMode.LOOP
        }

        _listMode.value = newMode

        setListMode.execute(newMode)
    }

    fun onPlayModeChanged() {
        val newMode = when(_playMode.value) {
            PlayMode.ORDERED -> PlayMode.SHUFFLE
            PlayMode.SHUFFLE -> PlayMode.ORDERED
            else -> PlayMode.ORDERED
        }

        _playMode.value = newMode

        _currentPlayingSong.value?.let {
            loadPlaylist(_playlist.value, it)
        }

        savePlaybackState()
        setPlayMode.execute(newMode)
    }

    fun loadPlaybackState() {
        viewModelScope.launch {
            _playListLoadingState.value = false

            log("AudioViewModel", "Loading playback state")
            val state = getPlaybackState.execute()
            log("AudioViewModel", "Loaded playback state: ${state?.currentIndex}, ${state?.currentPosition}")

            state?.let {
                val basePlaylist = it.playlist.map { song ->
                    song.copy(album = song.album.copy(artwork = null))
                }

                _currentPosition.value = it.currentPosition
                _playlist.value = basePlaylist
                _currentPlayingSongIndexInPlaylist.value = it.currentIndex
                _mediaState.value = MediaState.PAUSED
                _playlistName.value = it.playlistName

                val index = it.currentIndex
                _previousCurrentPlayingSongAlbum.value = null

                if (basePlaylist.isNotEmpty() && index != null && index in basePlaylist.indices) {
                    val currentSong = basePlaylist[index]

                    val songWithArtwork = if (isExternalPath(currentSong.path)) {
                        currentSong.album.artworkHash?.let { hash ->
                            val result = withContext(Dispatchers.IO) {
                                getAlbumArtwork.execute(hash, isExternal = true)
                            }

                            if (result != null) {
                                currentSong.copy(
                                    album = currentSong.album.copy(artwork = result)
                                )
                            } else {
                                currentSong
                            }
                        } ?: currentSong
                    } else {
                        currentSong
                    }

                    _currentPlayingSong.value = songWithArtwork

                    putSongInPlayerType.execute(songWithArtwork, it.currentPosition)
                } else {
                    log("AudioViewModel", "No valid song to restore from playback state")
                    _currentPlayingSong.value = null
                }

                viewModelScope.launch(Dispatchers.IO) {
                    it.playlist.forEachIndexed { i, song ->
                        var artworkBytes = song.album.artworkHash?.let { hash ->
                            getAlbumArtwork.execute(hash)
                        }
                        if (artworkBytes == null) {
                            artworkBytes = song.album.artworkHash?.let { hash ->
                                getAlbumArtwork.execute(hash, isExternal = true)
                            }
                        }
                        val updatedSong = song.copy(album = song.album.copy(artwork = artworkBytes))

                        _playlist.update { old ->
                            old.toMutableList().apply {
                                if (i in indices) {
                                    set(i, updatedSong)
                                }
                            }
                        }

                        if (i == it.currentIndex && _currentPlayingSong.value?.album?.artwork == null) {
                            _currentPlayingSong.value = updatedSong
                            log("AudioViewModel", "Updated current song artwork in background: size=${artworkBytes?.size ?: 0}")
                        }
                    }
                    log("AudioViewModel", "Artwork loading finished for ${it.playlist.size} songs")
                }
            }
            _playListLoadingState.value = true
        }
    }


    @OptIn(FlowPreview::class)
    private fun observeStateChanges() {
        viewModelScope.launch {
            currentPosition
                .sample(5000)
                .collect { pos ->
                    val state = PlaybackState(
                        playlist = _playlist.value,
                        currentPosition = pos,
                        currentIndex = _currentPlayingSongIndexInPlaylist.value,
                        playlistName = _playlistName.value
                    )
                    log("AudioViewModel", "Saving playback state from observe: ${state.currentIndex}, ${state.currentPosition}")
                    savePlaybackState.execute(state)
                }
        }
    }

    fun savePlaybackState() {
        log("AudioViewModel", "Saving playback state")
        val state = PlaybackState(
            playlist = _playlist.value,
            currentPosition = _currentPosition.value,
            currentIndex = _currentPlayingSongIndexInPlaylist.value,
            playlistName = _playlistName.value
        )
        log("AudioViewModel", "Saving playback state: ${state.currentIndex}, ${state.currentPosition}")
        savePlaybackState.execute(state)
    }

    fun setPlaylistName(name: String) {
        _playlistName.value = name
        savePlaybackState()
    }
}