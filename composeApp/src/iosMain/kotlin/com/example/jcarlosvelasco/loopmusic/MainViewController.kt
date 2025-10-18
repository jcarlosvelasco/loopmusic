package com.example.jcarlosvelasco.loopmusic

import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.CreateNavGraph
import com.example.jcarlosvelasco.loopmusic.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController() = ComposeUIViewController {
    val themeScreenViewModel: ThemeViewModel = koinViewModel()

    AppTheme(
        themeScreenViewModel = themeScreenViewModel,
        content = {
            val navController = rememberNavController()
            CreateNavGraph(navController)
        }
    )
}