package com.example.jcarlosvelasco.loopmusic.ui.features.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@Composable
fun OtherScreen(
    navController: NavHostController,
    remainingScreenFeatures: List<SCREEN_FEATURES>?
) {
    Scaffold {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            Text(
                stringResource(Res.string.others_header),
                style = appTypography().headlineLarge
            )

            Spacer(modifier = Modifier.padding(12.dp))

            remainingScreenFeatures?.let { features ->
                LazyColumn {
                    for (item in features) {
                        item {
                            Text(
                                text = stringResource(item.titleRes),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 8.dp)
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