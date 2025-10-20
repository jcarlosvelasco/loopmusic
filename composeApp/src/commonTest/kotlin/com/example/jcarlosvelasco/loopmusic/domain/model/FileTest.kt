package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class FileTest {

    @Test
    fun `file creation with path and modification date`() {
        val file = File(
            path = "/music/song.mp3",
            modificationDate = 1234567890L
        )

        assertEquals("/music/song.mp3", file.path)
        assertEquals(1234567890L, file.modificationDate)
    }

    @Test
    fun `files with different modification dates`() {
        val oldFile = File(
            path = "/old.mp3",
            modificationDate = 1000000000L
        )

        val newFile = File(
            path = "/new.mp3",
            modificationDate = 2000000000L
        )

        assertEquals(1000000000L, oldFile.modificationDate)
        assertEquals(2000000000L, newFile.modificationDate)
    }
}
