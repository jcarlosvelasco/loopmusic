package com.example.jcarlosvelasco.loopmusic.infrastructure.database.converters

import androidx.room.TypeConverter
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity
import kotlinx.serialization.json.Json

class FolderEntityConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromFolderEntityList(value: List<FolderEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toFolderEntityList(value: String): List<FolderEntity> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromSelectionState(value: SelectionState): String {
        return value.name
    }

    @TypeConverter
    fun toSelectionState(value: String): SelectionState {
        return SelectionState.valueOf(value)
    }

    @TypeConverter
    fun fromFolder(value: FolderEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toFolder(value: String?): FolderEntity? {
        return value?.let { json.decodeFromString(it) }
    }
}