package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesPickerInfrType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.utils.log
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.path

class FilePicker(
    private val files: FilesInfrType
): FilesPickerInfrType {
    override suspend fun openDirectoryPicker(): Folder? {
        val value = FileKit.openDirectoryPicker() ?: return null

        files.takePersistablePermissionIfNeeded(value.path)
        return buildFolderTree(value.path)
    }

    fun decodeUriComponent(encoded: String): String {
        return encoded
            .replace("%3A", ":")
            .replace("%2F", "/")
            .replace("+", " ")
            .replace("%20", " ")
    }

    private fun getFolderName(path: String): String {
        val result = decodeUriComponent(path)
        log("FilePicker", "getFolderName: $result")
        return result.substringAfterLast("/")
    }

    override fun buildFolderTree(path: String): Folder {
        val subPaths = files.listSubdirectories(path)
            .filter { !getFolderName(it).startsWith(".") }

        log("FilePicker", subPaths.toString())

        val subfolders = subPaths.map { subPath -> buildFolderTree(subPath) }

        return Folder(
            path = path,
            name = getFolderName(path),
            subfolders = subfolders
        )
    }
}