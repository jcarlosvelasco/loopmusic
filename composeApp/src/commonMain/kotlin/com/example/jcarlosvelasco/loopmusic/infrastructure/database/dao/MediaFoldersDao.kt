package com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity

@Dao
interface MediaFoldersDao {
    @Insert
    suspend fun insertMediaFolders(folders: List<FolderEntity>)

    @Query("SELECT * FROM media_folders")
    suspend fun getSelectedMediaFolders(): List<FolderEntity>

    @Query("DELETE FROM media_folders")
    suspend fun deleteAll()

    @Query("SELECT * FROM media_folders WHERE path = :path")
    suspend fun getFolderFromPath(path: String): FolderEntity? {
        return getSelectedMediaFolders().firstOrNull { it.path == path }
    }
}