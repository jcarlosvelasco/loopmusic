package com.example.jcarlosvelasco.loopmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcarlosvelasco.loopmusic.domain.model.Platform
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.utils.getPlatform
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private val DarkColorScheme = darkColorScheme(
    primaryContainer = primary_dark,
    surface = elevation_dark,
    primary = font_color_dark,
    background = background_dark,
    onSurfaceVariant = muted_dark,
    onPrimaryContainer = onPrimary_dark,
    surfaceVariant = elevation2_dark,
    onPrimary = font_color_light
)

private val LightColorScheme = lightColorScheme(
    primaryContainer = primary_light,
    surface = elevation_light,
    primary = font_color_light,
    background = background_light,
    onSurfaceVariant = muted_light,
    onPrimaryContainer = onPrimary_light,
    surfaceVariant = elevation2_light,
    onPrimary = font_color_dark
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
    themeScreenViewModel: ThemeViewModel = koinViewModel(),
) {
    val theme by themeScreenViewModel.theme.collectAsStateWithLifecycle()
    val statusBarManager: StatusBarManagerType = koinInject()
    val platform = getPlatform()

    val systemIsDark = if (platform == Platform.ANDROID) isSystemInDarkTheme() else statusBarManager.isSystemInDarkTheme()

    val isDarkMode = when (theme) {
        Theme.DARK -> true
        Theme.LIGHT -> false
        Theme.SYSTEM -> systemIsDark
        null -> systemIsDark
    }

    val colorScheme = when (theme) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.SYSTEM -> if (systemIsDark) DarkColorScheme else LightColorScheme
        null -> if (systemIsDark) DarkColorScheme else LightColorScheme
    }

    LaunchedEffect(isDarkMode) {
        statusBarManager.setStatusBarStyle(isDarkMode)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}