package com.example.jcarlosvelasco.loopmusic.presentation.navigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetSelectedMediaFoldersType
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MainRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MediaFoldersRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.NavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigatorViewModel(
    private val getSelectedMediaFolders: GetSelectedMediaFoldersType
): ViewModel() {
    private val _startDestination: MutableStateFlow<NavigationRoute?> = MutableStateFlow(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isEmpty = getSelectedMediaFolders.isEmpty()
            _startDestination.value = if (isEmpty) MediaFoldersRoute(fromSettings = false) else MainRoute
        }
    }
}