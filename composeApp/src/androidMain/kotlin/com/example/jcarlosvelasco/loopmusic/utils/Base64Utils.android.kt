package com.example.jcarlosvelasco.loopmusic.utils

import android.util.Base64

actual fun String.encodeBase64(): String {
    return Base64.encodeToString(
        this.toByteArray(Charsets.UTF_8),
        Base64.NO_WRAP
    )
}

actual fun String.decodeBase64(): String {
    return Base64.decode(this, Base64.NO_WRAP)
        .toString(Charsets.UTF_8)
}