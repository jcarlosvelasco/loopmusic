package com.example.jcarlosvelasco.loopmusic.ui.features.settings_tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jcarlosvelasco.loopmusic.ui.features.main.NavigationTab
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun DummyCustomNavigationItem(
    tab: NavigationTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val title = stringResource(tab.titleRes)

    Surface(
        onClick = onClick,
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = appTypography().bodyMedium
            )
        }
    }
}