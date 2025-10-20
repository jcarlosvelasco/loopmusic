package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SongTest {

    @Test
    fun `song creation with all fields`() {
        val artist = Artist(id = 1, name = "The Beatles")
        val album = Album(
            id = 1,
            name = "Abbey Road",
            artist = artist,
            artworkHash = "hash123",
            year = 1969
        )

        val song = Song(
            path = "/music/beatles/abbey_road/come_together.mp3",
            name = "Come Together",
            artist = artist,
            modificationDate = 1234567890L,
            album = album,
            duration = 259000L,
            trackNumber = 1
        )

        assertEquals("/music/beatles/abbey_road/come_together.mp3", song.path)
        assertEquals("Come Together", song.name)
        assertEquals("The Beatles", song.artist.name)
        assertEquals(1234567890L, song.modificationDate)
        assertEquals("Abbey Road", song.album.name)
        assertEquals(259000L, song.duration)
        assertEquals(1, song.trackNumber)
    }

    @Test
    fun `song creation without track number`() {
        val artist = Artist(id = 1, name = "Unknown Artist")
        val album = Album(
            id = 1,
            name = "Unknown Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val song = Song(
            path = "/music/unknown.mp3",
            name = "Unknown Song",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 180000L,
            trackNumber = null
        )

        assertNull(song.trackNumber)
    }

    @Test
    fun `song with different durations`() {
        val artist = Artist(id = 1, name = "Test Artist")
        val album = Album(
            id = 1,
            name = "Test Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val shortSong = Song(
            path = "/short.mp3",
            name = "Short",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 30000L,
            trackNumber = null
        )

        val longSong = Song(
            path = "/long.mp3",
            name = "Long",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 600000L,
            trackNumber = null
        )

        assertEquals(30000L, shortSong.duration)
        assertEquals(600000L, longSong.duration)
    }
}
