package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesPickerInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MetadataParserType
import com.example.jcarlosvelasco.loopmusic.data.mapper.AudioMetadataMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.FolderEntityMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.FolderMapper
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.*
import com.example.jcarlosvelasco.loopmusic.domain.model.File
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.MediaFoldersDao

class FilesRepository(
    private val openDirectoryPicker: FilesPickerInfrType,
    private val filesDao: MediaFoldersDao,
    private val entityMapper: FolderEntityMapper,
    private val folderMapper: FolderMapper,
    private val files: FilesInfrType,
    private val metadataParser: MetadataParserType,
    private val audioMetadataMapper: AudioMetadataMapper
):
    GetSelectedMediaFoldersRepositoryType,
    OpenDirectoryPickerRepositoryType,
    StoreSelectedMediaFoldersRepositoryType,
    ReadFileFromPathRepositoryType,
    GetFilesFromFolderRepositoryType,
    GivePermissionsRepositoryType,
    BuildFolderTreeRepositoryType
{
    override suspend fun getSelectedMediaFolders(): List<Folder> {
        return filesDao.getSelectedMediaFolders().map { entityMapper.mapToFolder(it) }
    }

    override suspend fun openDirectoryPicker(): Folder? {
        return openDirectoryPicker.openDirectoryPicker()
    }

    override suspend fun storeSelectedMediaFolders(folders: List<Folder>, deletePrevious: Boolean) {
        if (deletePrevious) {
            filesDao.deleteAll()
        }

        val entities = folders.map { folderMapper.mapToEntity(it) }
        filesDao.insertMediaFolders(entities)
    }

    override suspend fun getFilesFromFolder(folder: Folder): List<File> {
        return files.listFiles(folder.path)
    }

    override suspend fun readFile(path: File): Song {
        val info = metadataParser.parseMetadata(path.path)
        val song = audioMetadataMapper.mapToSong(path, info)
        return song
    }

    override fun givePermissions(path: String) {
        files.takePersistablePermissionIfNeeded(path)
    }

    override fun buildFolderTree(path: String): Folder {
        return openDirectoryPicker.buildFolderTree(path)
    }
}