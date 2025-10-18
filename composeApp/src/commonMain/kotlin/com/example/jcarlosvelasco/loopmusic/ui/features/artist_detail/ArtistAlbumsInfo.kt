package com.example.jcarlosvelasco.loopmusic.ui.features.artist_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.ui.features.albums.AlbumItem
import com.example.jcarlosvelasco.loopmusic.ui.navigation.AlbumDetailRoute
import com.example.jcarlosvelasco.loopmusic.ui.navigation.safeNavigate
import com.example.jcarlosvelasco.loopmusic.ui.theme.appTypography
import loopmusic.composeapp.generated.resources.Res
import loopmusic.composeapp.generated.resources.artist_detail_albums
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistAlbumsInfo(
    albums: List<Album>,
    navController: NavHostController,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(Res.string.artist_detail_albums), style = appTypography().headlineMedium)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (album in albums) {
                AlbumItem(
                    album,
                    Modifier.padding(bottom = 16.dp),
                    onClick = {
                        safeNavigate(navController, AlbumDetailRoute(album.id))
                    }
                )
            }
        }
    }
}