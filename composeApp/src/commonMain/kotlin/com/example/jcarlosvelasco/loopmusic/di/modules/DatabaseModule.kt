package com.example.jcarlosvelasco.loopmusic.di.modules

import com.example.jcarlosvelasco.loopmusic.di.factories.DatabaseFactory
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.AppDatabase
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.AlbumDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.ArtistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.MediaFoldersDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.PlaylistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.SongDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class DatabaseModule {
    @Single
    fun provideDatabase(): AppDatabase {
        return DatabaseFactory().createDatabase()
    }

    @Single
    fun provideMediaFoldersDao(database: AppDatabase): MediaFoldersDao {
        return database.getMediaFoldersDao()
    }

    @Single
    fun provideSongDao(database: AppDatabase): SongDao {
        return database.getSongDao()
    }

    @Single
    fun provideArtistDao(database: AppDatabase): ArtistDao {
        return database.getArtistDao()
    }

    @Single
    fun provideAlbumDao(database: AppDatabase): AlbumDao {
        return database.getAlbumDao()
    }

    @Single
    fun providePlaylistDao(database: AppDatabase): PlaylistDao {
        return database.getPlaylistDao()
    }
}
