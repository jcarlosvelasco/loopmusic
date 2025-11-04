package com.example.jcarlosvelasco.loopmusic.ui.skeleton

import ShimmerTextSkeleton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.albumsongsinfo_songs
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlbumSongsInfoSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ShimmerTextSkeleton(
            textStyle = appTypography().headlineMedium,
            width = 100.dp
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(10) {
                SongItemSkeleton()
            }
        }
    }
}