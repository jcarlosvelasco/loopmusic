package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetFilesFromFolderRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.ReadFileFromPathRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ReadFilesFromFoldersType {
    suspend fun execute(folders: List<Folder>): Flow<Song>
}


class ReadFilesFromFolders(
    private val getFilesFromFolderRepo: GetFilesFromFolderRepositoryType,
    private val readFileFromPath: ReadFileFromPathRepositoryType
): ReadFilesFromFoldersType {

    override suspend fun execute(folders: List<Folder>): Flow<Song> = flow {
        for (folder in folders) {
            val files = getFilesFromFolderRepo.getFilesFromFolder(folder)
            for (filePath in files) {
                val song = readFileFromPath.readFile(filePath)
                if (song != null) {
                    emit(song)
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}