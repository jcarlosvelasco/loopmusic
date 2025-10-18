package com.example.jcarlosvelasco.loopmusic.utils

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.NSData

@OptIn(BetaInteropApi::class)
actual fun String.encodeBase64(): String {
    val nsString = NSString.create(string = this)
    val data = nsString.dataUsingEncoding(NSUTF8StringEncoding)
        ?: throw IllegalArgumentException("Failed to encode string to UTF-8")

    return data.base64EncodedStringWithOptions(0u)
}

@OptIn(BetaInteropApi::class)
actual fun String.decodeBase64(): String {
    val data = NSData.create(base64EncodedString = this, options = 0u)
        ?: throw IllegalArgumentException("Invalid Base64 string")

    val decodedNSString = NSString.create(data = data, encoding = NSUTF8StringEncoding)
        ?: throw IllegalArgumentException("Failed to decode Base64 to UTF-8 string")

    return decodedNSString.toString()
}