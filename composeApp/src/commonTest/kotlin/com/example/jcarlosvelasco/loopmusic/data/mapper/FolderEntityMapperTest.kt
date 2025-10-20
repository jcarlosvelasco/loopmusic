package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.SelectionState
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.FolderEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FolderEntityMapperTest {

    private val mapper = FolderEntityMapper()

    @Test
    fun `mapToFolder maps simple entity correctly`() {
        val entity = FolderEntity(
            path = "/music/rock",
            name = "rock",
            subfolders = emptyList(),
            selectionState = SelectionState.UNSELECTED,
            rootParent = null
        )

        val folder = mapper.mapToFolder(entity)

        assertEquals("/music/rock", folder.path)
        assertEquals("rock", folder.name)
        assertEquals(emptyList(), folder.subfolders)
        assertEquals(SelectionState.UNSELECTED, folder.selectionState)
        assertNull(folder.rootParent)
    }

    @Test
    fun `mapToFolder maps entity with subfolders`() {
        val subEntity1 = FolderEntity(
            path = "/music/rock/metal",
            name = "metal"
        )
        val subEntity2 = FolderEntity(
            path = "/music/rock/punk",
            name = "punk"
        )
        
        val entity = FolderEntity(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(subEntity1, subEntity2)
        )

        val folder = mapper.mapToFolder(entity)

        assertEquals(2, folder.subfolders.size)
        assertEquals("/music/rock/metal", folder.subfolders[0].path)
        assertEquals("metal", folder.subfolders[0].name)
        assertEquals("/music/rock/punk", folder.subfolders[1].path)
        assertEquals("punk", folder.subfolders[1].name)
    }

    @Test
    fun `mapToFolder maps entity with different selection states`() {
        val unselectedEntity = FolderEntity(
            path = "/music/rock",
            name = "rock",
            selectionState = SelectionState.UNSELECTED
        )

        val selectedEntity = FolderEntity(
            path = "/music/pop",
            name = "pop",
            selectionState = SelectionState.SELECTED
        )

        val partialEntity = FolderEntity(
            path = "/music/jazz",
            name = "jazz",
            selectionState = SelectionState.PARTIAL
        )

        assertEquals(SelectionState.UNSELECTED, mapper.mapToFolder(unselectedEntity).selectionState)
        assertEquals(SelectionState.SELECTED, mapper.mapToFolder(selectedEntity).selectionState)
        assertEquals(SelectionState.PARTIAL, mapper.mapToFolder(partialEntity).selectionState)
    }

    @Test
    fun `mapToFolder maps entity with root parent`() {
        val rootEntity = FolderEntity(
            path = "/music",
            name = "music"
        )
        
        val childEntity = FolderEntity(
            path = "/music/rock",
            name = "rock",
            rootParent = rootEntity
        )

        val folder = mapper.mapToFolder(childEntity)

        assertEquals("/music", folder.rootParent?.path)
        assertEquals("music", folder.rootParent?.name)
    }

    @Test
    fun `mapToFolder maps nested entity hierarchy`() {
        val level3Entity = FolderEntity(path = "/music/rock/metal/thrash", name = "thrash")
        val level2Entity = FolderEntity(
            path = "/music/rock/metal",
            name = "metal",
            subfolders = listOf(level3Entity)
        )
        val level1Entity = FolderEntity(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(level2Entity)
        )

        val folder = mapper.mapToFolder(level1Entity)

        assertEquals(1, folder.subfolders.size)
        assertEquals("metal", folder.subfolders[0].name)
        assertEquals(1, folder.subfolders[0].subfolders.size)
        assertEquals("thrash", folder.subfolders[0].subfolders[0].name)
    }
}
