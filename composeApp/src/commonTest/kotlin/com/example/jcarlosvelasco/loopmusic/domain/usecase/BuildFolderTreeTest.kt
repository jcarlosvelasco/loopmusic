package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.BuildFolderTreeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildFolderTreeTest {

    private class MockBuildFolderTreeRepository : BuildFolderTreeRepositoryType {
        var lastCalledPath: String? = null
        var folderToReturn: Folder? = null

        override fun buildFolderTree(path: String): Folder {
            lastCalledPath = path
            return folderToReturn ?: Folder(path = path, name = path.split("/").last())
        }
    }

    @Test
    fun `execute calls repository with correct path`() {
        val mockRepo = MockBuildFolderTreeRepository()
        val useCase = BuildFolderTree(mockRepo)

        useCase.execute("/music/rock")

        assertEquals("/music/rock", mockRepo.lastCalledPath)
    }

    @Test
    fun `execute returns folder from repository`() {
        val expectedFolder = Folder(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(
                Folder(path = "/music/rock/metal", name = "metal"),
                Folder(path = "/music/rock/punk", name = "punk")
            ),
            selectionState = SelectionState.UNSELECTED
        )

        val mockRepo = MockBuildFolderTreeRepository().apply {
            folderToReturn = expectedFolder
        }
        val useCase = BuildFolderTree(mockRepo)

        val result = useCase.execute("/music/rock")

        assertEquals("/music/rock", result.path)
        assertEquals("rock", result.name)
        assertEquals(2, result.subfolders.size)
        assertEquals("metal", result.subfolders[0].name)
        assertEquals("punk", result.subfolders[1].name)
    }

    @Test
    fun `execute works with different paths`() {
        val mockRepo = MockBuildFolderTreeRepository()
        val useCase = BuildFolderTree(mockRepo)

        useCase.execute("/documents")
        assertEquals("/documents", mockRepo.lastCalledPath)

        useCase.execute("/photos/vacation")
        assertEquals("/photos/vacation", mockRepo.lastCalledPath)

        useCase.execute("/videos/movies/action")
        assertEquals("/videos/movies/action", mockRepo.lastCalledPath)
    }

    @Test
    fun `execute returns folder with empty subfolders when repository returns such folder`() {
        val folderWithNoSubfolders = Folder(
            path = "/music/empty",
            name = "empty",
            subfolders = emptyList()
        )

        val mockRepo = MockBuildFolderTreeRepository().apply {
            folderToReturn = folderWithNoSubfolders
        }
        val useCase = BuildFolderTree(mockRepo)

        val result = useCase.execute("/music/empty")

        assertEquals(0, result.subfolders.size)
    }
}
