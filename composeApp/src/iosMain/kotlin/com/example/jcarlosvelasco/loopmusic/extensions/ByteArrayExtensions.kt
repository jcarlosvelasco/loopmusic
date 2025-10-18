package com.example.jcarlosvelasco.loopmusic.extensions


import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.MediaPlayer.MPMediaItemArtwork
import platform.UIKit.UIImage


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toUIImage(): UIImage = this.usePinned { pinned ->
    val data = NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
    UIImage(data = data)
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toMediaArtwork(): MPMediaItemArtwork {
    val image = this.toUIImage()
    return MPMediaItemArtwork(boundsSize = image.size) { _ ->
        image
    }
}