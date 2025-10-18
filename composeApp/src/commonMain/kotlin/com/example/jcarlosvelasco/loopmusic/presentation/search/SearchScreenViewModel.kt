package com.example.jcarlosvelasco.loopmusic.presentation.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchScreenViewModel: ViewModel() {
    private val _isTextFieldFocused: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTextFieldFocused = _isTextFieldFocused.asStateFlow()

    private val _isSongsButtonActive: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isSongsButtonActive = _isSongsButtonActive.asStateFlow()

    private val _isAlbumsButtonActive: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isAlbumsButtonActive = _isAlbumsButtonActive.asStateFlow()

    private val _isArtistsButtonActive: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isArtistsButtonActive = _isArtistsButtonActive.asStateFlow()

    fun updateTextFieldFocus(value: Boolean) {
        _isTextFieldFocused.value = value
    }

    fun updateSongsButtonActive(value: Boolean) {
        _isSongsButtonActive.value = value
    }

    fun updateAlbumsButtonActive(value: Boolean) {
        _isAlbumsButtonActive.value = value
    }

    fun updateArtistsButtonActive(value: Boolean) {
        _isArtistsButtonActive.value = value
    }
}