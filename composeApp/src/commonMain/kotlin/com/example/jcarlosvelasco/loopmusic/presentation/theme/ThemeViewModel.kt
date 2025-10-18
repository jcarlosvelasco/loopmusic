package com.example.jcarlosvelasco.loopmusic.presentation.theme

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetThemeType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.SetThemeType
import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(
    private val getTheme: GetThemeType,
    private val setTheme: SetThemeType,
    private val statusBarManager: StatusBarManagerType
): ViewModel() {

    private val _theme: MutableStateFlow<Theme?> = MutableStateFlow(null)
    val theme = _theme.asStateFlow()

    init {
        log("ThemeViewModel", "Init")
        loadTheme()
    }

    fun loadTheme() {
        val value = getTheme.execute()
        log("ThemeViewModel", "Loaded theme: $value")
        if (value == null) {
            val newTheme = Theme.SYSTEM
            _theme.value = newTheme
            setTheme.execute(newTheme)
        }
        else {
            _theme.value = value
        }
        updateStatusBar(value ?: Theme.SYSTEM)
    }

    private fun updateStatusBar(currentTheme: Theme) {
        val isDark = when (currentTheme) {
            Theme.DARK -> true
            Theme.LIGHT -> false
            Theme.SYSTEM -> statusBarManager.isSystemInDarkTheme()
        }
        statusBarManager.setStatusBarStyle(isDark)
    }

    fun setTheme(newTheme: Theme) {
        _theme.value = newTheme
        updateStatusBar(newTheme)
        setTheme.execute(newTheme)
    }
}