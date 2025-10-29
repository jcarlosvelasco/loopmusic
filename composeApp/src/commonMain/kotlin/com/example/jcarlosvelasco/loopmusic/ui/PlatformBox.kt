package com.example.jcarlosvelasco.loopmusic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.domain.model.Platform
import com.example.jcarlosvelasco.loopmusic.presentation.platform.PlatformObject

@Composable
fun PlatformBox(
    content: @Composable () -> Unit
) {
    Box(
        modifier = if (PlatformObject.platform == Platform.IOS) {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
            } else {
                Modifier.fillMaxSize()
            }
    ) {
        content()
    }
}