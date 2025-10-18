package com.example.jcarlosvelasco.loopmusic.ui.features.mediaFolders.widgets

import MediaFolderItem
import androidx.compose.runtime.Composable
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder

@Composable
fun HierarchicalFolderList(
    folders: List<Folder>,
    onCheckedChange: (Folder) -> Unit,
    onRemove: (Folder) -> Unit,
    onToggleExpansion: (Folder) -> Unit,
    level: Int = 0
) {
    folders.forEach { folder ->
        MediaFolderItem(
            folder = folder,
            onCheckedChange = onCheckedChange,
            onRemove = onRemove,
            onToggleExpansion = onToggleExpansion,
            level = level
        )

        // Only show subfolders if the folder is expanded
        if (folder.isExpanded && folder.subfolders.isNotEmpty()) {
            HierarchicalFolderList(
                folders = folder.subfolders,
                onCheckedChange = onCheckedChange,
                onRemove = onRemove,
                onToggleExpansion = onToggleExpansion,
                level = level + 1
            )
        }
    }
}