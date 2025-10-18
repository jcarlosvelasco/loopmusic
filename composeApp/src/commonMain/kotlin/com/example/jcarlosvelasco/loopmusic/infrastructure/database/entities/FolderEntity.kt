package com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "media_folders")
data class FolderEntity(
    @PrimaryKey val path: String,
    val name: String,
    var subfolders: List<FolderEntity> = emptyList(),
    var selectionState: SelectionState = SelectionState.UNSELECTED,
    val rootParent: FolderEntity? = null
)