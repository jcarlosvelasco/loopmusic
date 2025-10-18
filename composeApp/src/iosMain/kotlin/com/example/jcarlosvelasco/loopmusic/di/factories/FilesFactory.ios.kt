package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.Files
import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType

actual class FilesFactory actual constructor() {
    actual fun getFiles(): FilesInfrType {
        return Files()
    }
}