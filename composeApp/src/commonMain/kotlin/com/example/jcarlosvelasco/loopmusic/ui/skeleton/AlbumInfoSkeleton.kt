package com.example.jcarlosvelasco.loopmusic.ui.skeleton

import ShimmerTextSkeleton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography

@Composable
fun AlbumInfoSkeleton(
    isLandscape: Boolean
) {
    Column(
        modifier = if (isLandscape) Modifier else Modifier.fillMaxWidth(),
        horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ShimmerTextSkeleton(
            textStyle = appTypography().headlineMedium,
            width = if (isLandscape) 50.dp else 150.dp
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        ShimmerTextSkeleton(
            textStyle = appTypography().bodyMedium,
            width = if (isLandscape) 50.dp else 150.dp
        )
    }
}