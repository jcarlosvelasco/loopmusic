package com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.presentation.main.MainScreenViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.mediaFolders.MediaFoldersScreenViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MainRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MediaFoldersRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.media_folders_add
import loopmusic.composeapp.generated.resources.media_folders_continue
import loopmusic.composeapp.generated.resources.media_folders_save
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomButtons(
    buttonsModifier: Modifier = Modifier,
    scope: CoroutineScope,
    mediaFolders: List<Folder>?,
    navController: NavHostController,
    viewModel: MediaFoldersScreenViewModel,
    mainViewModel: MainScreenViewModel,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            launcher.launch(intent)
        },
        modifier = buttonsModifier,
        content = {
            Text(
                stringResource(Res.string.media_folders_add),
                style = appTypography().bodyLarge
            )
        }
    )

    if (viewModel.fromSettings) {
        Button(
            onClick = {
                scope.launch {
                    viewModel.storeFolders()
                    mainViewModel.loadSongs()
                    safePopBackStack(navController)
                }
            },
            modifier = buttonsModifier,
            content = { Text(stringResource(Res.string.media_folders_save), style = appTypography().bodyLarge) },
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
            modifier = buttonsModifier,
            content = { Text(stringResource(Res.string.media_folders_continue),
                style = appTypography().bodyLarge) },
            enabled = !mediaFolders.isNullOrEmpty() && mediaFolders.any { it.selectionState == SelectionState.SELECTED || it.selectionState == SelectionState.PARTIAL }
        )
    }
}