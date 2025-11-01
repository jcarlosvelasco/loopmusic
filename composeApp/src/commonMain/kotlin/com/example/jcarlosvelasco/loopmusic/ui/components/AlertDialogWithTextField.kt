package com.example.jcarlosvelasco.loopmusic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.playlists_create_dialog_cancel
import loopmusic.composeapp.generated.resources.playlists_create_dialog_confirm
import loopmusic.composeapp.generated.resources.playlists_create_dialog_w_smth
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertDialogWithTextField(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    initialValue: String = ""
) {
    var textFieldValue by remember { mutableStateOf(initialValue) }

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
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.playlists_create_dialog_w_smth), style = appTypography().bodyMedium) },
                    textStyle = appTypography().bodyMedium
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(textFieldValue)
                }
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