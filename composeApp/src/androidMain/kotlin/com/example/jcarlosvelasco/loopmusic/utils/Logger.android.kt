package com.example.jcarlosvelasco.loopmusic.utils

import android.util.Log

actual fun log(tag: String, message: String) {
    if (IS_DEBUG) {
        Log.d(tag, message)
    }
}