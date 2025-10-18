package com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.AlbumEntity

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums WHERE albumId IN (:ids)")
    suspend fun getAlbumsByIds(ids: List<Long>): List<AlbumEntity>
}