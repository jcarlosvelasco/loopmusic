package com.example.jcarlosvelasco.loopmusic.ui.skeleton

import ShimmerTextSkeleton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography

@Composable
fun FeaturedArtistSongsSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerTextSkeleton(textStyle = appTypography().headlineMedium, width = 100.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(10) {
                SongItemSkeleton()
            }
        }
    }
}