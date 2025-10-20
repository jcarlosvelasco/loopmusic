package com.example.jcarlosvelasco.loopmusic.presentation.audio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListModeTest {

    @Test
    fun `ListMode has all expected values`() {
        val values = ListMode.entries
        
        assertEquals(3, values.size)
        assertTrue(values.contains(ListMode.ONE_SONG))
        assertTrue(values.contains(ListMode.ONE_TIME))
        assertTrue(values.contains(ListMode.LOOP))
    }

    @Test
    fun `ListMode valueOf works correctly`() {
        assertEquals(ListMode.ONE_SONG, ListMode.valueOf("ONE_SONG"))
        assertEquals(ListMode.ONE_TIME, ListMode.valueOf("ONE_TIME"))
        assertEquals(ListMode.LOOP, ListMode.valueOf("LOOP"))
    }

    @Test
    fun `ListMode values are distinct`() {
        val oneSong = ListMode.ONE_SONG
        val oneTime = ListMode.ONE_TIME
        val loop = ListMode.LOOP
        
        assertTrue(oneSong != oneTime)
        assertTrue(oneTime != loop)
        assertTrue(oneSong != loop)
    }
}
