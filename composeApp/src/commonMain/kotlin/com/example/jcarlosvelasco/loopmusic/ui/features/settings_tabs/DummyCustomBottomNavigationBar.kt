package com.example.jcarlosvelasco.loopmusic.ui.features.settings_tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.features.main.NavigationTab

@Composable
fun DummyCustomBottomNavigationBar(
    modifier: Modifier = Modifier,
    navigationTabs: List<NavigationTab>
) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationTabs.forEach { tab ->
                DummyCustomNavigationItem(
                    tab = tab,
                    selected = false,
                    onClick = {  }
                )
            }
            DummyCustomNavigationItem(
                tab = NavigationTab.OTHER,
                selected = false,
                onClick = {  }
            )
        }
    }
}