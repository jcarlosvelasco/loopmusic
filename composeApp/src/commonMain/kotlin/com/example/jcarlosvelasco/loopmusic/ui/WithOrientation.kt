package com.example.jcarlosvelasco.loopmusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun WithOrientation(
    content: @Composable (isLandscape: Boolean) -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    with(density) {
        val screenWidthDp = windowInfo.containerSize.width.toDp()
        val screenHeightDp = windowInfo.containerSize.height.toDp()
        val isLandscape = screenWidthDp > screenHeightDp

        content(isLandscape)
    }
}