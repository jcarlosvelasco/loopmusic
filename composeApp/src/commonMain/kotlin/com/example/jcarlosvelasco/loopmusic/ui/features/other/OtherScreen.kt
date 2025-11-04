package com.example.jcarlosvelasco.loopmusic.ui.features.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES.Companion.getRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.others_header
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen(
    navController: NavHostController,
    remainingScreenFeatures: List<SCREEN_FEATURES>?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.others_header),
                        style = appTypography().headlineLarge
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                    navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                    titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
                    actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
                    subtitleContentColor = TopAppBarDefaults.topAppBarColors().subtitleContentColor
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            remainingScreenFeatures?.let { features ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (item in features) {
                        item {
                            Text(
                                text = stringResource(item.titleRes),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        safeNavigate(navController, item.getRoute(true))
                                    },
                                style = appTypography().headlineMedium
                            )
                        }
                    }
                }
            }
        }
    }
}