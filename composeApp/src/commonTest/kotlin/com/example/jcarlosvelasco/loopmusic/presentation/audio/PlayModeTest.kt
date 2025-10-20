package com.example.jcarlosvelasco.loopmusic.presentation.audio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayModeTest {

    @Test
    fun `PlayMode has all expected values`() {
        val values = PlayMode.entries
        
        assertEquals(2, values.size)
        assertTrue(values.contains(PlayMode.ORDERED))
        assertTrue(values.contains(PlayMode.SHUFFLE))
    }

    @Test
    fun `PlayMode valueOf works correctly`() {
        assertEquals(PlayMode.ORDERED, PlayMode.valueOf("ORDERED"))
        assertEquals(PlayMode.SHUFFLE, PlayMode.valueOf("SHUFFLE"))
    }

    @Test
    fun `PlayMode values are distinct`() {
        val ordered = PlayMode.ORDERED
        val shuffle = PlayMode.SHUFFLE
        
        assertTrue(ordered != shuffle)
    }
}
