package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType


expect class MetadataParserFactory() {
    fun getMetadataParser(): MetadataParserType
}