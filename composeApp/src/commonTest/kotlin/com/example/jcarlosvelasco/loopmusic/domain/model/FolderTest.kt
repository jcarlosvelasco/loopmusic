package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class FolderTest {

    @Test
    fun `folder creation with default values`() {
        val folder = Folder(
            path = "/music/rock",
            name = "rock"
        )

        assertEquals("/music/rock", folder.path)
        assertEquals("rock", folder.name)
        assertEquals(emptyList(), folder.subfolders)
        assertFalse(folder.isExpanded)
        assertEquals(SelectionState.UNSELECTED, folder.selectionState)
        assertNull(folder.rootParent)
    }

    @Test
    fun `folder with subfolders`() {
        val subfolder1 = Folder(path = "/music/rock/metal", name = "metal")
        val subfolder2 = Folder(path = "/music/rock/punk", name = "punk")
        
        val folder = Folder(
            path = "/music/rock",
            name = "rock",
            subfolders = listOf(subfolder1, subfolder2)
        )

        assertEquals(2, folder.subfolders.size)
        assertEquals("metal", folder.subfolders[0].name)
        assertEquals("punk", folder.subfolders[1].name)
    }

    @Test
    fun `folder with expanded state`() {
        val folder = Folder(
            path = "/music/rock",
            name = "rock",
            isExpanded = true
        )

        assertEquals(true, folder.isExpanded)
    }

    @Test
    fun `folder with selection states`() {
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

        assertEquals(SelectionState.UNSELECTED, unselectedFolder.selectionState)
        assertEquals(SelectionState.SELECTED, selectedFolder.selectionState)
        assertEquals(SelectionState.PARTIAL, partialFolder.selectionState)
    }

    @Test
    fun `folder with root parent`() {
        val rootFolder = Folder(path = "/music", name = "music")
        val childFolder = Folder(
            path = "/music/rock",
            name = "rock",
            rootParent = rootFolder
        )

        assertEquals("/music", childFolder.rootParent?.path)
        assertEquals("music", childFolder.rootParent?.name)
    }
}
