package com.example.jcarlosvelasco.loopmusic.ui.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.search_placeholder
import org.jetbrains.compose.resources.stringResource


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchBarText: String,
    setSearchBarText: (String) -> Unit,
    setSearchBarActive: (Boolean) -> Unit,
    searchBarActive: Boolean,
    onClearClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = searchBarText,
        onValueChange = {
            setSearchBarText(it)
        },
        singleLine = true,

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

        keyboardActions = KeyboardActions(
            onSearch = {
                // Hide the keyboard and clear focus
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),

        modifier = modifier
            .onFocusChanged {
                setSearchBarActive(it.isFocused)
            }
            .clip(RoundedCornerShape(24.dp))
        ,

        textStyle = appTypography().bodyLarge + TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer),

        placeholder = {
            Text(
                stringResource(Res.string.search_placeholder),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = appTypography().bodyLarge
            )
        },

        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = when {
                    searchBarActive -> Icons.AutoMirrored.Filled.ArrowBack
                    else -> Icons.Default.Search
                },
                contentDescription = "Search Icon",
                modifier = Modifier.clickable {
                    if (searchBarActive) {
                        setSearchBarActive(false)
                        onClearClick()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        trailingIcon = {
            if (searchBarText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    )
}