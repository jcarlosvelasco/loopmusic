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
fun SongItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Column {
            ShimmerTextSkeleton(
                textStyle = appTypography().bodyLarge,
                width = 80.dp
            )

            Spacer(modifier = Modifier.padding(2.dp))

            ShimmerTextSkeleton(
                textStyle = appTypography().bodySmall,
                width = 60.dp
            )
        }
    }
}