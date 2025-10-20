package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Folder
import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FolderMapperTest {

    private val mapper = FolderMapper()

    @Test
    fun `mapToEntity maps simple folder correctly`() {
        val folder = Folder(
            path = "/music/rock",
            name = "rock",
            subfolders = emptyList(),
            isExpanded = false,
            selectionState = SelectionState.UNSELECTED,
            rootParent = null
        )

        val entity = mapper.mapToEntity(folder)

        assertEquals("/music/rock", entity.path)
        assertEquals("rock", entity.name)
        assertEquals(emptyList(), entity.subfolders)
        assertEquals(SelectionState.UNSELECTED, entity.selectionState)
        assertNull(entity.rootParent)
    }

    @Test
    fun `mapToEntity maps folder with subfolders`() {
        val subfolder1 = Folder(
            path = "/music/rock/metal",
            name = "metal"
        )
        val subfolder2 = Folder(
            path = "/music/rock/punk",
            name = "punk"
        )
        
        val folder = Folder(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(subfolder1, subfolder2)
        )

        val entity = mapper.mapToEntity(folder)

        assertEquals(2, entity.subfolders.size)
        assertEquals("/music/rock/metal", entity.subfolders[0].path)
        assertEquals("metal", entity.subfolders[0].name)
        assertEquals("/music/rock/punk", entity.subfolders[1].path)
        assertEquals("punk", entity.subfolders[1].name)
    }

    @Test
    fun `mapToEntity maps folder with different selection states`() {
        val unselectedFolder = Folder(
            path = "/music/rock",
            name = "rock",
            selectionState = SelectionState.UNSELECTED
        )

        val selectedFolder = Folder(
            path = "/music/pop",
            name = "pop",
            selectionState = SelectionState.SELECTED
        )

        val partialFolder = Folder(
            path = "/music/jazz",
            name = "jazz",
            selectionState = SelectionState.PARTIAL
        )

        assertEquals(SelectionState.UNSELECTED, mapper.mapToEntity(unselectedFolder).selectionState)
        assertEquals(SelectionState.SELECTED, mapper.mapToEntity(selectedFolder).selectionState)
        assertEquals(SelectionState.PARTIAL, mapper.mapToEntity(partialFolder).selectionState)
    }

    @Test
    fun `mapToEntity maps folder with root parent`() {
        val rootFolder = Folder(
            path = "/music",
            name = "music"
        )
        
        val childFolder = Folder(
            path = "/music/rock",
            name = "rock",
            rootParent = rootFolder
        )

        val entity = mapper.mapToEntity(childFolder)

        assertEquals("/music", entity.rootParent?.path)
        assertEquals("music", entity.rootParent?.name)
    }

    @Test
    fun `mapToEntity maps nested folder hierarchy`() {
        val level3Folder = Folder(path = "/music/rock/metal/thrash", name = "thrash")
        val level2Folder = Folder(
            path = "/music/rock/metal",
            name = "metal",
            subfolders = listOf(level3Folder)
        )
        val level1Folder = Folder(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(level2Folder)
        )

        val entity = mapper.mapToEntity(level1Folder)

        assertEquals(1, entity.subfolders.size)
        assertEquals("metal", entity.subfolders[0].name)
        assertEquals(1, entity.subfolders[0].subfolders.size)
        assertEquals("thrash", entity.subfolders[0].subfolders[0].name)
    }
}
