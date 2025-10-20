package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaybackStateTest {

    @Test
    fun `playback state with empty playlist`() {
        val state = PlaybackState(
            playlist = emptyList(),
            currentPosition = 0f,
            currentIndex = null,
            playlistName = "My Playlist"
        )

        assertTrue(state.playlist.isEmpty())
        assertEquals(0f, state.currentPosition)
        assertNull(state.currentIndex)
        assertEquals("My Playlist", state.playlistName)
    }

    @Test
    fun `playback state with songs`() {
        val artist = Artist(id = 1, name = "Test Artist")
        val album = Album(
            id = 1,
            name = "Test Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val songs = listOf(
            Song(
                path = "/song1.mp3",
                name = "Song 1",
                artist = artist,
                modificationDate = 0L,
                album = album,
                duration = 180000L,
                trackNumber = 1
            ),
            Song(
                path = "/song2.mp3",
                name = "Song 2",
                artist = artist,
                modificationDate = 0L,
                album = album,
                duration = 200000L,
                trackNumber = 2
            )
        )

        val state = PlaybackState(
            playlist = songs,
            currentPosition = 45.5f,
            currentIndex = 0,
            playlistName = "Rock Playlist"
        )

        assertEquals(2, state.playlist.size)
        assertEquals(45.5f, state.currentPosition)
        assertEquals(0, state.currentIndex)
        assertEquals("Rock Playlist", state.playlistName)
    }

    @Test
    fun `playback state with different positions`() {
        val state1 = PlaybackState(
            playlist = emptyList(),
            currentPosition = 0f,
            currentIndex = null,
            playlistName = "Test"
        )

        val state2 = PlaybackState(
            playlist = emptyList(),
            currentPosition = 120.75f,
            currentIndex = 5,
            playlistName = "Test"
        )

        assertEquals(0f, state1.currentPosition)
        assertEquals(120.75f, state2.currentPosition)
        assertNull(state1.currentIndex)
        assertEquals(5, state2.currentIndex)
    }
}
