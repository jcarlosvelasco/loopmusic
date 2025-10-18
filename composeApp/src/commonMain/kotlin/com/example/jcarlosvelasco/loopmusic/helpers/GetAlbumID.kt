package com.example.jcarlosvelasco.loopmusic.helpers

fun getAlbumID(albumName: String?, artistName: String?): Long {
    return "${albumName ?: "undefined_artist"}_${artistName ?: "undefined_album"}".hashCode().toLong()
}