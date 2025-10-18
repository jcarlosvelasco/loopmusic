package com.example.jcarlosvelasco.loopmusic.ui.features.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp
        )
    }
}