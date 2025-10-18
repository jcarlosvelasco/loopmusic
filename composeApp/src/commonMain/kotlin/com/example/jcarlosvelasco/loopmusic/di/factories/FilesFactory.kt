package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType

expect class FilesFactory() {
    fun getFiles(): FilesInfrType
}