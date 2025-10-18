package com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao

import androidx.room.*
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.SongWithArtistAndAlbum

@Dao
interface SongDao {

    @Transaction
    @Query("SELECT * FROM songs")
    suspend fun getSongsWithArtist(): List<SongWithArtistAndAlbum>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(list: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>): List<Long>

    @Query("SELECT DISTINCT artworkHash FROM albums WHERE artworkHash IS NOT NULL")
    suspend fun getAllArtworkHashesInDb(): List<String>

    //TODO: If we delete all the artists songs, artist won't get deleted
    @Query("DELETE FROM songs WHERE path IN (:paths)")
    suspend fun deleteSongsByPaths(paths: List<String>)

    @Query("Select * from albums where albumId = :albumId")
    suspend fun getAlbumFromAlbumId(albumId: Long): AlbumEntity

    @Query("Select * from artists where artistId = :artistId")
    suspend fun getArtistFromArtistId(artistId: Long): ArtistEntity

    @Query("Select * from songs where albumId = :albumId")
    suspend fun getSongsFromAlbumId(albumId: Long): List<SongEntity>
}