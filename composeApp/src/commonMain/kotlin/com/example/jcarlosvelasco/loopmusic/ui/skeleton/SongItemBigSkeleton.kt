package com.example.jcarlosvelasco.loopmusic.ui.skeleton

import ShimmerTextSkeleton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography

@Composable
fun SongItemBigSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Column(
            modifier = Modifier.width(100.dp)
        ) {
            ShimmerTextSkeleton(
                textStyle = appTypography().bodyLarge,
                width = 100.dp
            )

            Spacer(modifier = Modifier.padding(2.dp))

            ShimmerTextSkeleton(
                textStyle = appTypography().bodySmall,
                width = 80.dp
            )
        }
    }
}
