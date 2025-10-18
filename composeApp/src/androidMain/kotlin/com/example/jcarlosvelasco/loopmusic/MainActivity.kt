package com.example.jcarlosvelasco.loopmusic

import android.Manifest
import android.content.ComponentName
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
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.rememberNavController
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme
import com.example.jcarlosvelasco.loopmusic.infrastructure.PlaybackService
import com.example.jcarlosvelasco.loopmusic.presentation.theme.ThemeViewModel
import com.example.jcarlosvelasco.loopmusic.ui.navigation.CreateNavGraph
import com.example.jcarlosvelasco.loopmusic.ui.theme.AppTheme
import com.example.jcarlosvelasco.loopmusic.utils.log
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0,
            )
        }

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
                }
            )
        }
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

    override fun onStop() {
        super.onStop()
        mediaController?.release()
        mediaController = null

        controllerFuture?.cancel(true)
        controllerFuture = null
    }
}