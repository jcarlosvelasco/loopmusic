package com.example.jcarlosvelasco.loopmusic.di.factories

import android.content.Context
import com.example.jcarlosvelasco.loopmusic.infrastructure.Files
import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class FilesFactory actual constructor(): KoinComponent {
    actual fun getFiles(): FilesInfrType {
        val context: Context by inject()
        return Files(context)
    }
}