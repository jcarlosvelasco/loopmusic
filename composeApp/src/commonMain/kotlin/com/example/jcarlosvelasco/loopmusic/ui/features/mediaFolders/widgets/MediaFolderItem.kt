import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MediaFolderItem(
    folder: Folder,
    onCheckedChange: (Folder) -> Unit,
    onRemove: (Folder) -> Unit,
    onToggleExpansion: (Folder) -> Unit,
    level: Int = 0
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (level * 16).dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (folder.subfolders.isNotEmpty()) {
                IconButton(
                    onClick = { onToggleExpansion(folder) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Text(
                        text = if (folder.isExpanded) "▼" else "▶",
                        style = appTypography().bodySmall
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(24.dp))
            }

            Text(
                text = "📁",
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Text(
                text = folder.name,
                style = appTypography().bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f, fill = false)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onRemove(folder) },
                content = {
                    Icon(Icons.Filled.Delete, "Remove")
                }
            )

            TriStateCheckbox(
                state = when (folder.selectionState) {
                    SelectionState.SELECTED -> ToggleableState.On
                    SelectionState.UNSELECTED -> ToggleableState.Off
                    SelectionState.PARTIAL -> ToggleableState.Indeterminate
                },
                onClick = { onCheckedChange(folder) }
            )
        }
    }
}