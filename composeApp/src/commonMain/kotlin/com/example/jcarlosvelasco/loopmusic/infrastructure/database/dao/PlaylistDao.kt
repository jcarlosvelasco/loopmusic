package com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao

import androidx.room.*
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistEntity
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistSongCrossRef
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistWithSongs

@Dao
interface PlaylistDao {

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistWithSongs(playlistId: Long): PlaylistWithSongs?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongs(crossRefs: List<PlaylistSongCrossRef>)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)

    @Query("UPDATE playlists SET name = :playListName WHERE id = :playlistId")
    suspend fun renamePlaylist(playlistId: Long, playListName: String)

    @Delete
    suspend fun removeSongFromPlaylist(crossRefs: List<PlaylistSongCrossRef>)
}