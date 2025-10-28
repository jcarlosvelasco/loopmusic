package com.example.jcarlosvelasco.loopmusic

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.rememberNavController
import com.example.jcarlosvelasco.loopmusic.data.repositories.FilesRepository
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.domain.usecase.CacheAlbumArtworkType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetAlbumArtworkType
import com.example.jcarlosvelasco.loopmusic.infrastructure.PlaybackService
import com.example.jcarlosvelasco.loopmusic.presentation.audio.AudioViewModel
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.CreateNavGraph
import com.example.jcarlosvelasco.loopmusic.ui.navigation.PlayingRoute
import com.example.jcarlosvelasco.loopmusic.ui.theme.AppTheme
import com.example.jcarlosvelasco.loopmusic.utils.let2
import com.example.jcarlosvelasco.loopmusic.utils.log
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin

class MainActivity : ComponentActivity() {
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var pendingNavigationUri: Uri? = null

    companion object {
        private const val KEY_PENDING_URI = "pendingUri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permisos si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0,
            )
        }

        // Restaurar URI pendiente tras rotación
        pendingNavigationUri = savedInstanceState?.getParcelable(KEY_PENDING_URI)

        setContent {
            val themeScreenViewModel: ThemeViewModel = koinViewModel()
            val currentTheme by themeScreenViewModel.theme.collectAsStateWithLifecycle()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            val shouldUseDarkTheme = when (currentTheme) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.SYSTEM -> isSystemInDarkTheme
                null -> isSystemInDarkTheme
            }

            LaunchedEffect(shouldUseDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = android.graphics.Color.TRANSPARENT,
                        darkScrim = android.graphics.Color.TRANSPARENT,
                        detectDarkMode = { shouldUseDarkTheme }
                    )
                )
            }

            AppTheme(
                themeScreenViewModel = themeScreenViewModel,
                content = {
                    val navController = rememberNavController()
                    CreateNavGraph(navController)

                    LaunchedEffect(pendingNavigationUri) {
                        pendingNavigationUri?.let { uri ->
                            handleAudioFile(uri, navController)
                            pendingNavigationUri = null
                        }
                    }
                }
            )
        }

        // Solo manejar el intent si la Activity se crea por primera vez
        if (savedInstanceState == null) {
            handleIncomingIntent(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingNavigationUri?.let { outState.putParcelable(KEY_PENDING_URI, it) }
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture?.addListener(
            {
                try {
                    mediaController = controllerFuture?.get()
                } catch (e: Exception) {
                    log("MainActivity", "Error: ${e.message}")
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data ?: return

            // Tomar persistable permission si la intent lo permite
            try {
                val takeFlags = intent.flags and
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                contentResolver.takePersistableUriPermission(uri, takeFlags)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

            val fileName = uri.lastPathSegment ?: "Desconocido"
            log("MainActivity", "File name: $fileName")
            pendingNavigationUri = uri
        }
    }

    private suspend fun copyToInternalStorage(uri: Uri): java.io.File {
        val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "audio_${System.currentTimeMillis()}.mp3"
        val file = java.io.File(filesDir, fileName)

        withContext(Dispatchers.IO) {
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    private fun handleAudioFile(uri: Uri, navController: androidx.navigation.NavController) {
        log("MainActivity", "handleAudioFile: $uri")
        val filesRepository: FilesRepository = getKoin().get()
        val audioViewModel: AudioViewModel = getKoin().get()
        val cacheAlbumArtwork: CacheAlbumArtworkType = getKoin().get()
        val getAlbumArtwork: GetAlbumArtworkType = getKoin().get()

        lifecycleScope.launch {
            try {
                val internalFile = copyToInternalStorage(uri)
                val internalUri = Uri.fromFile(internalFile)

                val song = filesRepository.readFile(File(internalUri.toString(), modificationDate = 0L))

                if (song.album.artwork == null) {
                    withContext(Dispatchers.IO) {
                        song.album.artworkHash?.let {
                            log("MainActivity", "Fetching artwork for ${song.name}")
                            val result = getAlbumArtwork.execute(it, isExternal = true)
                            song.album.artwork = result
                        }
                    }
                } else {
                    // Cache the artwork
                    withContext(Dispatchers.IO) {
                        let2(song.album.artworkHash, song.album.artwork) { hash, artwork ->
                            log("MainActivity", "Caching artwork for ${song.name}")
                            cacheAlbumArtwork.execute(identifier = hash, image = artwork)
                        }
                    }
                }

                audioViewModel.loadPlaylistAndPlay(listOf(song), isShuffled = false)

                navController.navigate(PlayingRoute) {
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                log("MainActivity", "Error handling audio file: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mediaController?.release()
        mediaController = null

        controllerFuture?.cancel(true)
        controllerFuture = null
    }
}
