package com.example.jcarlosvelasco.loopmusic.helpers

fun getArtistID(artistName: String?): Long {
    return (artistName ?: "undefined").hashCode().toLong()
}