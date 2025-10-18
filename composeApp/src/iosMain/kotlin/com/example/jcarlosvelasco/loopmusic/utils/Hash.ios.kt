package com.example.jcarlosvelasco.loopmusic.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH

@OptIn(ExperimentalForeignApi::class)
actual fun sha256(bytes: ByteArray): String {
    val result = UByteArray(CC_SHA256_DIGEST_LENGTH)
    bytes.usePinned { pinned ->
        memScoped {
            CC_SHA256(
                data = pinned.addressOf(0),
                len = bytes.size.convert(),
                md = result.refTo(0)
            )
        }
    }
    val sb = StringBuilder(result.size * 2)
    for (b in result) {
        sb.append((b.toInt() and 0xFF).toString(16).padStart(2, '0'))
    }
    return sb.toString()
}