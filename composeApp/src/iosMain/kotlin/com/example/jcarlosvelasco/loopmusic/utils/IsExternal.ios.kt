package com.example.jcarlosvelasco.loopmusic.utils

import platform.Foundation.*

actual fun isExternalPath(path: String): Boolean {
    val fileManager = NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
    val documentsUrl = (urls.firstOrNull() as? NSURL) ?: return true
    val documentsPath = documentsUrl.path ?: return true

    if (path.startsWith(documentsPath)) return true
    if (path.contains("File Provider Storage")) return false
    return true
}
