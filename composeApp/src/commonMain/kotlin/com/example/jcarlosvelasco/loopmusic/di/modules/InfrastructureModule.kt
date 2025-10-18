package com.example.jcarlosvelasco.loopmusic.di.modules

import com.example.jcarlosvelasco.loopmusic.data.interfaces.*
import com.example.jcarlosvelasco.loopmusic.di.factories.*
import com.example.jcarlosvelasco.loopmusic.infrastructure.CommonMetadataParser
import com.example.jcarlosvelasco.loopmusic.infrastructure.FilePicker
import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType
import com.example.jcarlosvelasco.loopmusic.infrastructure.networking.KtorHttpClient
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class InfrastructureModule {
    @Factory
    fun getSharedPreferences(): SharedPreferencesInfrType {
        return SharedPreferencesFactory().getSharedPreferences()
    }

    @Factory
    fun getFiles(): FilesInfrType {
        return FilesFactory().getFiles()
    }

    @Factory
    fun getMetadataParser(): MetadataParserType {
        return MetadataParserFactory().getMetadataParser()
    }

    @Single
    fun getFilePicker(
        files: FilesInfrType
    ): FilesPickerInfrType {
        return FilePicker(
            files
        )
    }

    @Single
    fun getCommonMetadataParser(): CommonMetadataParser {
        return CommonMetadataParser()
    }

    @Single
    fun getMediaPlayer(): MediaPlayerInfrType {
        return MediaPlayerFactory().getPlayer()
    }

    @Single
    fun getPlaybackService(): PlaybackServiceType {
        return PlaybackServiceFactory().createPlaybackService()
    }

    @Single
    fun getGetStatusBarManager(): StatusBarManagerType {
        return StatusBarFactory().getStatusBarManager()
    }

    @Single
    fun getHttpClient(): HttpClientType {
        return KtorHttpClient()
    }
}