package com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.mediaFolders.MediaFoldersScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun MediaFoldersScreen(
    navController: NavHostController,
    viewModel: MediaFoldersScreenViewModel = koinViewModel(),
    mainViewModel: MainScreenViewModel = koinViewModel()
)