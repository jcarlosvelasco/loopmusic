package com.example.jcarlosvelasco.loopmusic.ui.features.folder_songs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography

@Composable
fun FolderItem(
    onClick: () -> Unit,
    folder: Folder
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
    ) {
        Text(folder.name, style = appTypography().headlineMedium)
    }
}