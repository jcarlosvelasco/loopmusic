package com.example.jcarlosvelasco.loopmusic.presentation.playing

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetFullQualityArtworkType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope

class PlayingScreenViewModel(
    private val getFullQualityArtwork: GetFullQualityArtworkType
): ViewModel() {
    private val _playingPillHeight: MutableStateFlow<Dp> = MutableStateFlow(0.dp)
    val playingPillHeight = _playingPillHeight.asStateFlow()

    private val _fullQualityArtwork: MutableStateFlow<ByteArray?> = MutableStateFlow(null)
    val fullQualityArtwork = _fullQualityArtwork.asStateFlow()

    var lastProcessedAlbum: Album? = null

    private val _dominantColor = MutableStateFlow<Color?>(null)
    val dominantColor: StateFlow<Color?> = _dominantColor.asStateFlow()

    private val _dominantOnColor = MutableStateFlow<Color?>(null)
    val dominantOnColor: StateFlow<Color?> = _dominantOnColor.asStateFlow()

    private val isMenuExpanded = MutableStateFlow(false)
    val isMenuExpandedFlow = isMenuExpanded.asStateFlow()

    // Use a custom CoroutineScope that survives app restarts
    private val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun updateDominantColors(color: Color, onColor: Color) {
        _dominantColor.value = color
        _dominantOnColor.value = onColor
    }

    fun formatTime(time: Float): String {
        val totalSeconds = (time / 1000).toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val minutesStr = minutes.toString().padStart(2, '0')
        val secondsStr = seconds.toString().padStart(2, '0')

        return if (hours > 0) {
            val hoursStr = hours.toString().padStart(2, '0')
            "$hoursStr:$minutesStr:$secondsStr"
        } else {
            "$minutesStr:$secondsStr"
        }
    }

    fun setPlayingPillHeight(height: Dp) {
        _playingPillHeight.value = height
    }

    fun getFullQualityArtwork(songPath: String): ByteArray? {
        customScope.launch {
            log("PlayingScreenViewModel", "Loading full quality artwork for: $songPath")
            _fullQualityArtwork.value = null
            val value = withContext(Dispatchers.IO) {
                getFullQualityArtwork.execute(songPath)
            }
            _fullQualityArtwork.value = value
        }
        return null
    }

    fun updateLastProcessedAlbum(album: Album) {
        lastProcessedAlbum = album
    }

    fun updateIsMenuExpanded(value: Boolean) {
        isMenuExpanded.value = value
    }
}