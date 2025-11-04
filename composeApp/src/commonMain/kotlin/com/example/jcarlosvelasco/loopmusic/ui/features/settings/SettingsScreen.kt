package com.example.jcarlosvelasco.loopmusic.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.MediaFoldersRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.SettingsTabsRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: ThemeViewModel = koinViewModel()
) {
    val selectedTheme by viewModel.theme.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_header), style = appTypography().headlineLarge) },
                navigationIcon = {
                    IconButton(
                        onClick = { safePopBackStack(navController) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
                    navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                    actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
                    scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.settings_theme),
                        style = appTypography().headlineMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (selectedTheme == Theme.LIGHT) {
                            Button(onClick = {
                                viewModel.setTheme(Theme.LIGHT)
                            }) {
                                Text(stringResource(Res.string.settings_light), style = appTypography().bodyLarge)
                            }
                        } else {
                            OutlinedButton(onClick = {
                                viewModel.setTheme(Theme.LIGHT)
                            }) {
                                Text(stringResource(Res.string.settings_light), style = appTypography().bodyLarge)
                            }
                        }
                        if (selectedTheme == Theme.DARK) {
                            Button(onClick = {
                                viewModel.setTheme(Theme.DARK)
                            }) {
                                Text(stringResource(Res.string.settings_dark), style = appTypography().bodyLarge)
                            }
                        } else {
                            OutlinedButton(onClick = {
                                viewModel.setTheme(Theme.DARK)
                            }) {
                                Text(stringResource(Res.string.settings_dark), style = appTypography().bodyLarge)
                            }
                        }
                        if (selectedTheme == Theme.SYSTEM) {
                            Button(onClick = {
                                viewModel.setTheme(Theme.SYSTEM)
                            }) {
                                Text(stringResource(Res.string.settings_system), style = appTypography().bodyLarge)
                            }
                        } else {
                            OutlinedButton(onClick = {
                                viewModel.setTheme(Theme.SYSTEM)
                            }) {
                                Text(stringResource(Res.string.settings_system), style = appTypography().bodyLarge)
                            }
                        }
                    }
                }

                SettingsRowItem(
                    text = stringResource(Res.string.settings_tabs),
                    onClick = {
                        navController.navigate(SettingsTabsRoute)
                    },
                )
                SettingsRowItem(
                    text = stringResource(Res.string.settings_media),
                    onClick = {
                        navController.navigate(MediaFoldersRoute(fromSettings = true))
                    },
                )
            }
        }
    }
}
