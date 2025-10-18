package com.example.jcarlosvelasco.loopmusic.di.factories

import android.content.Context
import com.example.jcarlosvelasco.loopmusic.infrastructure.CommonMetadataParser
import com.example.jcarlosvelasco.loopmusic.infrastructure.MetadataParser
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class MetadataParserFactory actual constructor(): KoinComponent {
    actual fun getMetadataParser(): MetadataParserType {
        val context: Context by inject()
        val commonMetadataParser: CommonMetadataParser by inject()
        return MetadataParser(context, commonMetadataParser)
    }
}