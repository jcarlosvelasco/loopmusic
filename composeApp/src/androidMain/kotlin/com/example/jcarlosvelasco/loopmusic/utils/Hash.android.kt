package com.example.jcarlosvelasco.loopmusic.utils

import java.security.MessageDigest

actual fun sha256(bytes: ByteArray): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    val sb = StringBuilder(digest.size * 2)
    for (b in digest) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}