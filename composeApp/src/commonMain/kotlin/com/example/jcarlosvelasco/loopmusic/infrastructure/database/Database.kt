package com.example.jcarlosvelasco.loopmusic.infrastructure.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.converters.FolderEntityConverter
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.AlbumDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.ArtistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.MediaFoldersDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.PlaylistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.SongDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistSongCrossRef
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@Database(
    entities = [
        FolderEntity::class,
        SongEntity::class,
        ArtistEntity::class,
        AlbumEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(FolderEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMediaFoldersDao(): MediaFoldersDao
    abstract fun getSongDao(): SongDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getAlbumDao(): AlbumDao
    abstract fun getArtistDao(): ArtistDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        //.addMigrations(MIGRATIONS)
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}