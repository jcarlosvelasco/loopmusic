package com.example.jcarlosvelasco.loopmusic.utils

import platform.posix.sysconf
import platform.posix._SC_NPROCESSORS_ONLN

actual fun availableProcessors(): Int {
    val cores = sysconf(_SC_NPROCESSORS_ONLN)
    return if (cores > 0) cores.toInt() else 1
}
