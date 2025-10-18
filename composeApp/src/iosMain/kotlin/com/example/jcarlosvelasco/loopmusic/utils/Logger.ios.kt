package com.example.jcarlosvelasco.loopmusic.utils

actual fun log(tag: String, message: String) {
    if (IS_DEBUG) {
        print("$tag: $message\n")
    }
}