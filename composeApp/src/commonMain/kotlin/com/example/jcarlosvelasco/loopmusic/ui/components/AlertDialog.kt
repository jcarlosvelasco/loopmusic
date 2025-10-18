package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.playlists_create_dialog_cancel
import loopmusic.composeapp.generated.resources.playlists_create_dialog_confirm
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle, style = appTypography().headlineMedium)
        },
        text = {
            Column {
                Text(text = dialogText, style = appTypography().bodyLarge)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(stringResource(Res.string.playlists_create_dialog_confirm), style = appTypography().bodyLarge)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.playlists_create_dialog_cancel),  style = appTypography().bodyLarge)
            }
        }
    )
}