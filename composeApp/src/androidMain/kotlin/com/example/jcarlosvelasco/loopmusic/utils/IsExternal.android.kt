package com.example.jcarlosvelasco.loopmusic.utils

actual fun isExternalPath(path: String): Boolean {
    return path.contains("file:///data/user/")
}