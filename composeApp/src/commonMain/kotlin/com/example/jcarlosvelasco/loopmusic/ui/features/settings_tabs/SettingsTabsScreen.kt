package com.example.jcarlosvelasco.loopmusic.ui.features.settings_tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.presentation.features.FeaturesViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safePopBackStack
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTabsScreen(
    navController: NavHostController,
    featuresViewModel: FeaturesViewModel = koinViewModel()
) {
    val currentScreenFeatures by featuresViewModel.selectedScreenFeatures.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.tabs_header),
                        style = appTypography().headlineLarge
                    )
                },
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
                .padding(top = 4.dp)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                DummyCustomBottomNavigationBar(
                    navigationTabs = featuresViewModel.selectedNavigationTabs
                )

                Spacer(modifier = Modifier.height(16.dp))

                featuresViewModel.remainingScreenFeatures?.let {
                    if (it.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            stringResource(Res.string.tabs_add),
                            style = appTypography().headlineMedium,
                        )
                        Column {
                            it.forEach { feature ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(feature.titleRes),
                                        style = appTypography().bodyMedium
                                    )
                                    Button(
                                        enabled = currentScreenFeatures != null && currentScreenFeatures!!.size < 4,
                                        onClick = {
                                            featuresViewModel.addFeature(feature)
                                        }
                                    ) {
                                        Text(
                                            stringResource(Res.string.add),
                                            style = appTypography().bodyLarge,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                currentScreenFeatures?.let {
                    if (it.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            stringResource(Res.string.tabs_current_tabs),
                            style = appTypography().headlineMedium,
                        )
                        Column {
                            it.forEachIndexed { index, feature ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(feature.titleRes),
                                        style = appTypography().bodyMedium
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            enabled = index > 0,
                                            onClick = {
                                                featuresViewModel.moveFeatureLeft(feature)
                                            },
                                            content = {
                                                Icon(Icons.Default.KeyboardArrowUp, "Up")
                                            }
                                        )

                                        IconButton(
                                            enabled = index < it.size - 1,
                                            onClick = {
                                                featuresViewModel.moveFeatureRight(feature)
                                            },
                                            content = {
                                                Icon(Icons.Default.KeyboardArrowDown, "Up")
                                            }
                                        )

                                        IconButton(
                                            enabled = feature != SCREEN_FEATURES.Home && currentScreenFeatures != null && currentScreenFeatures!!.size >= 2,
                                            onClick = {
                                                featuresViewModel.removeFeature(feature)
                                            },
                                            content = {
                                                Icon(Icons.Filled.Delete, "Remove")
                                            }
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
}