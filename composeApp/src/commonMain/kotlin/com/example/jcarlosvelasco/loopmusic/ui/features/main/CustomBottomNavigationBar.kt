package com.example.jcarlosvelasco.loopmusic.ui.features.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.utils.log

@Composable
fun CustomBottomNavigationBar(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier,
    navigationTabs: List<NavigationTab>
) {
    val density = LocalDensity.current

    val allTabs = remember(navigationTabs) {
        val shouldShowOther = navigationTabs.size < NavigationTab.entries.size - 1
        if (shouldShowOther) navigationTabs + NavigationTab.OTHER else navigationTabs
    }

    var indicatorOffsetX by remember { mutableStateOf(0.dp) }
    var indicatorWidth by remember { mutableStateOf(0.dp) }

    val animatedOffsetX by animateDpAsState(
        targetValue = indicatorOffsetX,
        animationSpec = tween(durationMillis = 300),
        label = "indicatorOffset"
    )

    val animatedWidth by animateDpAsState(
        targetValue = indicatorWidth,
        animationSpec = tween(durationMillis = 300),
        label = "indicatorWidth"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            if (animatedWidth > 0.dp) {
                Box(
                    modifier = Modifier
                        .offset(x = animatedOffsetX + 8.dp)
                        .width(animatedWidth)
                        .height(40.dp)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .align(Alignment.CenterStart)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                allTabs.forEach { tab ->
                    CustomNavigationItem(
                        tab = tab,
                        selected = selectedTab == tab,
                        onClick = {
                            log("CustomBottomNavigationBar", "Selected tab: $tab")
                            onTabSelected(tab)
                        },
                        onPositioned = { offsetX, width ->
                            if (selectedTab == tab) {
                                indicatorOffsetX = with(density) { offsetX.toDp() }
                                indicatorWidth = with(density) { width.toDp() }
                            }
                        }
                    )
                }
            }
        }
    }
}