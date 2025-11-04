package com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.mediaFolders.MediaFoldersScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.PlatformBox
import com.example.jcarlosvelasco.loopmusic.ui.WithOrientation
import com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders.widgets.HierarchicalFolderList
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MainRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MediaFoldersRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import kotlinx.coroutines.launch
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun MediaFoldersScreen(
    navController: NavHostController,
    viewModel: MediaFoldersScreenViewModel,
    mainViewModel: MainScreenViewModel
) {
    val scope = rememberCoroutineScope()

    val mediaFolders by viewModel.mediaFolders.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    WithOrientation { isLandscape ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(Res.string.media_folder),
                            style = appTypography().headlineLarge
                        )
                    },
                    navigationIcon = {
                        if (viewModel.fromSettings) {
                            IconButton(onClick = { safePopBackStack(navController) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                        navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                        titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
                        actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
                        subtitleContentColor = TopAppBarDefaults.topAppBarColors().subtitleContentColor
                    ),
                    actions = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp),
                                strokeWidth = 4.dp
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            mediaFolders == null -> {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        //TODO: Fix, it appears on top
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            mediaFolders!!.isEmpty() -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .wrapContentHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "📁",
                                                style = appTypography().headlineLarge,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                            Text(
                                                stringResource(Res.string.media_folders_empty),
                                                style = appTypography().bodyMedium
                                            )
                                            Text(
                                                stringResource(Res.string.media_folders_empty_tip),
                                                style = appTypography().bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            else -> {
                                item {
                                    HierarchicalFolderList(
                                        folders = mediaFolders!!,
                                        onCheckedChange = { viewModel.toggleFolderSelection(it) },
                                        onRemove = { viewModel.removeFolder(it) },
                                        onToggleExpansion = { viewModel.toggleFolderExpansion(it) }
                                    )
                                }
                            }
                        }
                    }

                    if (isLandscape) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.openDirectory()
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    stringResource(Res.string.media_folders_add),
                                    style = appTypography().bodyLarge
                                )
                            }

                            if (viewModel.fromSettings) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            viewModel.storeFolders()
                                            mainViewModel.loadSongs()
                                            safePopBackStack(navController)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    content = {
                                        Text(
                                            stringResource(Res.string.media_folders_save),
                                            style = appTypography().bodyLarge
                                        )
                                    },
                                )
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            viewModel.storeFolders()
                                            mainViewModel.loadSongs()
                                            navController.navigate(MainRoute) {
                                                popUpTo<MediaFoldersRoute> {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = !mediaFolders.isNullOrEmpty() &&
                                            mediaFolders!!.any {
                                                it.selectionState == SelectionState.SELECTED ||
                                                        it.selectionState == SelectionState.PARTIAL
                                            }
                                ) {
                                    Text(
                                        stringResource(Res.string.media_folders_continue),
                                        style = appTypography().bodyLarge
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.openDirectory()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    stringResource(Res.string.media_folders_add),
                                    style = appTypography().bodyLarge
                                )
                            }

                            if (viewModel.fromSettings) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            viewModel.storeFolders()
                                            mainViewModel.loadSongs()
                                            safePopBackStack(navController)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    content = {
                                        Text(
                                            stringResource(Res.string.media_folders_save),
                                            style = appTypography().bodyLarge
                                        )
                                    },
                                )
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            viewModel.storeFolders()
                                            mainViewModel.loadSongs()
                                            navController.navigate(MainRoute) {
                                                popUpTo<MediaFoldersRoute> {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !mediaFolders.isNullOrEmpty() &&
                                            mediaFolders!!.any {
                                                it.selectionState == SelectionState.SELECTED ||
                                                        it.selectionState == SelectionState.PARTIAL
                                            }
                                ) {
                                    Text(
                                        stringResource(Res.string.media_folders_continue),
                                        style = appTypography().bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
