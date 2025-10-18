package com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.ArtistEntity

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists WHERE artistId IN (:ids)")
    suspend fun getArtistsByIds(ids: List<Long>): List<ArtistEntity>
}